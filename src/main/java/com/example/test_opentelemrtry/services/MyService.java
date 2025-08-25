package com.example.test_opentelemrtry.services;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.test_opentelemrtry.aop.Traceable;
import com.example.test_opentelemrtry.dto.TransferRequestDto;
import com.example.test_opentelemrtry.models.Profile;
import com.example.test_opentelemrtry.models.User;
import com.example.test_opentelemrtry.repositories.UserRepository;
import com.example.test_opentelemrtry.utils.UtilA;
import com.example.test_opentelemrtry.utils.UtilB;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;

@Service
public class MyService {

    @Autowired
    private final UserRepository userRepository;

    // private final Tracer tracer =
    // GlobalOpenTelemetry.getTracer("io.opentelemetry.auto");
    private final UtilA utilA;
    private final UtilB utilB;
    private final LongCounter transferRequests;
    private final DoubleHistogram memoryHistogram;

    public MyService(UtilA utilA, UtilB utilB, UserRepository userRepository) {
        this.utilA = utilA;
        this.utilB = utilB;
        this.userRepository = userRepository;
        Meter meter = GlobalOpenTelemetry.getMeter("my-app");
        transferRequests = meter
                .counterBuilder("transaction_amount_total")
                .setDescription("จำนวน request ทั้งหมดที่เข้ามาใน transfer service")
                .setUnit("requests")
                .build();

        memoryHistogram = meter.histogramBuilder("transfer_service_jvm_memory_used_bytes")
                .setDescription("Histogram ของ memory usage")
                .setUnit("bytes")
                .build();

        // Register gauge: JVM memory usage (live monitoring)
        meter.gaugeBuilder("transfer_service_jvm_memory_used_gauge_bytes")
                .setDescription("Current used memory ของ JVM")
                .setUnit("bytes")
                .buildWithCallback(measurement -> {
                    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                    MemoryUsage heap = memoryBean.getHeapMemoryUsage();
                    measurement.record(heap.getUsed());
                });
    }

    @Traceable
    public Map<String, ?> getUsers() {

        // Span span = tracer.spanBuilder("MyService.getUsers")
        // .setParent(Context.current())
        // .startSpan();

        // RestTemplate restTemplate = new RestTemplate();
        // String url = "http://localhost:8001/components";

        int num = ThreadLocalRandom.current().nextInt(1, 6);
        num *= 1000;
        Map<String, Object> body = new HashMap<>();
        try {
            Thread.sleep(num);
            String dataA = utilA.compute();
            String dataB = utilB.compute();
            // ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // String message = dataA + dataB + response.getBody();
            List<User> users = userRepository.findAllWithProfile();

            body.put("message", dataA + dataB);
            body.put("users", users);

            return body;
        } catch (InterruptedException e) {
            // span.recordException(e);
            // e.printStackTrace();
        } finally {
            // span.setAttribute("myService.sleepDuration", num);
            // span.end();
        }

        body.put("message", "Error occurred while fetching users");
        return body;
    }

    @Traceable
    public void registerUser(User user) throws Exception {
        // Span span = tracer.spanBuilder("MyService.registerUser")
        // .setParent(Context.current())
        // .startSpan();
        try {
            String name = user.getEmail().split("@")[0];
            String avatarUrl = "http://fakeURL/" + name;
            Profile profile = new Profile();
            profile.setName(name);
            profile.setAvatarUrl(avatarUrl);

            profile.setUser(user);
            user.setProfile(profile);
            user.setBalance(10000);

            // save owner side
            userRepository.save(user);

        } catch (Exception e) {
            // span.recordException(e);
            throw new Exception(e);
        } finally {
            // span.end();
        }
    }

    @Traceable
    public String transferBalance(List<TransferRequestDto> transferDtos) throws Exception {

        try {
            for (TransferRequestDto transferDto : transferDtos) {
                User fromUser = userRepository.findById(transferDto.getFromId())
                        .orElseThrow(() -> new Exception("From user not found"));
                User toUser = userRepository.findById(transferDto.getToId())
                        .orElseThrow(() -> new Exception("To user not found"));

                if (fromUser.getBalance() < transferDto.getAmount()) {
                    throw new Exception("Insufficient balance for user ID: " + fromUser.getId());
                }
                fromUser.setBalance(fromUser.getBalance() - transferDto.getAmount());
                toUser.setBalance(toUser.getBalance() + transferDto.getAmount());

                userRepository.save(fromUser);
                userRepository.save(toUser);

                
            }
            transferRequests.add(transferDtos.size());
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heap = memoryBean.getHeapMemoryUsage();
            memoryHistogram.record(heap.getUsed());
            String transactionId = UUID.randomUUID().toString();

            Span.current().setAttribute("transaction.id", transactionId);

            return transactionId;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // public void testHeader(){
    // RestTemplate restTemplate = new RestTemplate();
    // String url = "http://localhost:8001/components";
    // ResponseEntity<String> response = restTemplate.getForEntity(url,
    // String.class);
    // }
}
