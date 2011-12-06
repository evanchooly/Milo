/**
 * Copyright (C) 2011 Justin Lee <jlee@antwerkz.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.milo.tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.milo.ServletContainer;
import org.milo.ServletListener;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dataProvider = "containers")
public class BasicServletTest extends MiloTestBase {
    public void deployServlet(ServletContainer container) throws IOException, ServletException {
        try {
            container.start();
            System.out.println("new File(\".\").getAbsolutePath() = " + new File(".").getAbsolutePath());
            container.createContext("ROOT", "/", "tests/basic/target/basic");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("http://localhost:" + 8080 + "/root"));
            final StatusLine statusLine = response.getStatusLine();
            Assert.assertNotEquals(statusLine.getStatusCode(), 404, "Should not have returned a 404");
            String body = read(response.getEntity().getContent());
            System.out.println("response = " + response);
            validate(response, "listener", ServletListener.class.getName());
            validate(response, "name", "value");
            validate(response, "name2", "value2");
            validate(response, "name3", "value3");
            Assert.assertTrue(body.startsWith("###BEFORE###"));
            Assert.assertTrue(body.endsWith("###AFTER###"));
        } finally {
            container.stop();
        }
    }

    private String read(InputStream content) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int read;
        final byte[] bytes = new byte[4096];
        while((read = content.read(bytes)) != -1) {
            stream.write(bytes, 0, read);
        }

        return new String(stream.toByteArray());
    }

    private void validate(HttpResponse responseBody, final String name, final String value) {
        final Header[] names = responseBody.getHeaders(name);
        Assert.assertTrue(names.length == 1 && names[0].getValue().equals(value));
    }

}
