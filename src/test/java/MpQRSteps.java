import com.uniheart.securing.web.wechat.mp.services.MpServiceBean;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources")
@CucumberContextConfiguration
//@SpringBootTest(classes = MpQRSteps.class)
public class MpQRSteps {
    private MpServiceBean mpServiceBean;

    @io.cucumber.java.en.When("^the client calls mp-qr$")
    public void theClientCallsMpQr() {
    }

    @io.cucumber.java.en.Then("^the client receives status code of (\\d+)$")
    public void theClientReceivesStatusCodeOf(int statusCode) {
        System.out.println(statusCode);
    }

    @io.cucumber.java.en.And("^the client receives the json$")
    public void theClientReceivesTheJson() {
    }

    @When("the mp-service calls weixin's access token endpoint")
    public void theMpServiceCallsWeixinSAccessTokenEndpoint() {
    }

    @Then("the mp-service gets the access token")
    public void theMpServiceGetsTheAccessToken() {
    }
}
