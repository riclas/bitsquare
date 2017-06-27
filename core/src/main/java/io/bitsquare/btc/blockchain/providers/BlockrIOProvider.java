package io.bitsquare.btc.blockchain.providers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.bitsquare.app.Log;
import io.bitsquare.http.HttpClient;
import io.bitsquare.http.HttpException;
import org.bitcoinj.core.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class BlockrIOProvider extends FeeProvider {
    private static final Logger log = LoggerFactory.getLogger(BlockrIOProvider.class);

    @Inject
    public BlockrIOProvider(HttpClient httpClient) {
        super(httpClient, "https://btc.blockr.io/api/v1/tx/info/");
    }

    @Override
    public Coin getFee(String transactionId) throws IOException, HttpException {
        Log.traceCall("transactionId=" + transactionId);
        try {
            JsonObject data = new JsonParser()
                    .parse(httpClient.requestWithGET(transactionId, "User-Agent", ""))
                    .getAsJsonObject()
                    .get("data")
                    .getAsJsonObject();
            return Coin.parseCoin(data
                    .get("fee")
                    .getAsString());
        } catch (IOException | HttpException e) {
            log.debug("Error at requesting transaction data from block explorer " + httpClient + "\n" +
                    "Error =" + e.getMessage());
            throw e;
        }
    }

    @Override
    public String toString() {
        return "BlockrIOProvider{" +
                '}';
    }
}
