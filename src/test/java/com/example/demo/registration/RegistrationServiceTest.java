package com.example.demo.registration;
import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import okhttp3.*;
import okhttp3.Request;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationServiceTest {


//    private static WireMockServer wireMockServer;
//
//    @MockBean
//    ConfirmationTokenService confirmationTokenService;
//
//    @Autowired
//    private RegistrationController registrationController;
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Before
//    public void startWireMock() {
//        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
//        wireMockServer.start();
//    }
//
//    @After
//    public void stopWireMock() {
//        wireMockServer.stop();
//    }
//
//    @Test
//    public void newUserTest() throws IOException {
//        String firstName = "Bob";
//        String lastName = "Lee";
//        String email = "Boblee@gmail.com";
//        String password = "bob";
//        RegistrationRequest userRequest = new RegistrationRequest("Bob", "Lee", "Boblee@gmail.com", "bob");
//        AppUser appUser = new AppUser(firstName, lastName, email, password, AppUserRole.USER);
//        ConfirmationToken token = new ConfirmationToken(appUser);
//
//        wireMockServer.stubFor(
//                WireMock.get(urlEqualTo("/api/v1/registration"))
//                    .willReturn(aResponse().
//                        withBody(token.getToken())));
//
//        System.out.println(wireMockServer.baseUrl());
//        System.out.println(WireMock.get("/api/v1/registration"));
//        assertTrue(wireMockServer.isRunning());
//
//
//        HttpClient httpclient = HttpClients.createDefault();
//        HttpPost httppost = new HttpPost(wireMockServer.baseUrl() + "/api/v1/registration");
//
//        List<NameValuePair> params = new ArrayList<NameValuePair>(4);
//        params.add(new BasicNameValuePair("firstN1ame", "Bob"));
//        params.add(new BasicNameValuePair("lastName", "Lee"));
//        params.add(new BasicNameValuePair("email", "Boblee@gmail.com"));
//        params.add(new BasicNameValuePair("password", "bob"));
//        httppost.setEntity(new UrlEncodedFormEntity(params));
//
//        CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httppost);
////        response.getEntity().
////        assertTrue(response.body().equals(token.getToken()));
//        System.out.println(response.getEntity().getContent());
//        System.out.println("=================");
//        System.out.println("=================");
//        System.out.println("=================");
//        System.out.println("=================");
//        System.out.println("=================");
//
//
//
//    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(9090));

    @Test
    public void wiremock_with_junit_test() throws Exception {

        String firstName = "Bob";
        String lastName = "Lee";
        String email = "boblee@gmail.com";
        String password = "bob";

        AppUser appUser = new AppUser(firstName, lastName, email, password, AppUserRole.USER);
        ConfirmationToken token = new ConfirmationToken(appUser);


        configureFor("localhost", 9090);
        stubFor(post(urlEqualTo("/api/v1/registration")).willReturn(aResponse().withBody(token.getToken())));

        RequestBody formBody = new FormBody.Builder()
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
                .add("password", password)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:9090/api/v1/registration")
                .method("POST", formBody)
                .build();

        Response response = client.newCall(request).execute();

        assertEquals(token.getToken(), Objects.requireNonNull(response.body()).string());
    }

}
