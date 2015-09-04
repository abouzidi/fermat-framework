package unit.com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.version_1.structure.HTTPJson;

import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.structure.HTTPJson;

import org.fest.assertions.api.Assertions;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Created by francisco on 03/09/15.
 */
public class GetJSONFromUrlTest {

    private JSONObject jsonObjectTest;
    HTTPJson htppJson = new HTTPJson();

        @Test
    public void TestGetJSONFromUrl_successful(){
        jsonObjectTest=htppJson.getJSONFromUrl("http://api.cryptocoincharts.info/tradingPair/btc_usd");
            String json;
            String jsonExpectedValue="btc/usd";
            json=jsonObjectTest.getString("id");
            Assertions.assertThat(json).isEqualTo(jsonExpectedValue);
    }
}
