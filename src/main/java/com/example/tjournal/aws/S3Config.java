package com.example.tjournal.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}

// @Bean을 사용하는 이유는 Spring 컨테이너에서 객체를 한 번만 생성하고, 전체 애플리케이션에서 재사용하기 위해서
// @Configuration 클래스에서 @Bean을 사용하면 Spring이 프로그램 시작 시 자동으로 객체를 생성 및 관리

// 객체 생성을 자동화
// @Bean을 사용하면 new를 직접 호출하지 않아도 Spring이 객체를 생성하고 관리합니다.
// 즉, S3Config 클래스의 amazonS3() 메서드를 직접 호출하지 않아도, Spring이 알아서 실행해줍니다.

// 객체의 재사용 (싱글톤 관리)
// Spring 컨테이너는 기본적으로 @Bean으로 생성된 객체를 싱글톤(하나의 인스턴스만 유지) 으로 관리합니다.
// 따라서 여러 곳에서 AmazonS3 객체를 필요로 해도, 같은 객체가 재사용됩니다.

// 의존성 주입 (Dependency Injection) 가능
// Spring이 관리하는 Bean은 @Autowired 또는 생성자 주입 등을 통해 필요한 곳에 자동으로 주입할 수 있습니다.

// Bean을 사용 안하면
// 객체가 필요할 때마다 매번 new를 호출해야 하므로 메모리 낭비가 발생