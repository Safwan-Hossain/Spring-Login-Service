package com.login.demo.registration;
import com.login.demo.appuser.AppUser;
import com.login.demo.appuser.AppUserRole;
import com.login.demo.registration.token.ConfirmationToken;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import okhttp3.*;
import okhttp3.Request;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
