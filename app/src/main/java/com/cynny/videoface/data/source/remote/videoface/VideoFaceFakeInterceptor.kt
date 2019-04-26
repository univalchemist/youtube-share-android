package com.cynny.videoface.data.source.remote.videoface

import com.cynny.videoface.BuildConfig
import okhttp3.*
import java.io.IOException


class VideoFaceFakeInterceptor : Interceptor {
    companion object {
        const val FAKE_RESPONSE = "[\n" +
                "  {\n" +
                "    \"counter\": 12,\n" +
                "    \"v_time\": 0,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.5,\n" +
                "      \"anger\": 0.5,\n" +
                "      \"disgust\": 0.0,\n" +
                "      \"fear\": 0.0,\n" +
                "      \"sadness\": 0.0,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.0\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"counter\": 1,\n" +
                "    \"v_time\": 1,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.2,\n" +
                "      \"anger\": 0.5,\n" +
                "      \"disgust\": 0.3,\n" +
                "      \"fear\": 0.0,\n" +
                "      \"sadness\": 0.0,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.0\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"counter\": 103,\n" +
                "    \"v_time\": 2,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.0,\n" +
                "      \"anger\": 0.5,\n" +
                "      \"disgust\": 0.5,\n" +
                "      \"fear\": 0.0,\n" +
                "      \"sadness\": 0.0,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.0\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"counter\": 1,\n" +
                "    \"v_time\": 13,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.5,\n" +
                "      \"anger\": 0.5,\n" +
                "      \"disgust\": 0.0,\n" +
                "      \"fear\": 0.0,\n" +
                "      \"sadness\": 0.0,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.0\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"counter\": 1,\n" +
                "    \"v_time\": 14,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.0,\n" +
                "      \"anger\": 0.5,\n" +
                "      \"disgust\": 0.0,\n" +
                "      \"fear\": 0.0,\n" +
                "      \"sadness\": 0.0,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.5\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"counter\": 1,\n" +
                "    \"v_time\": 15,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.2,\n" +
                "      \"anger\": 0.2,\n" +
                "      \"disgust\": 0.2,\n" +
                "      \"fear\": 0.2,\n" +
                "      \"sadness\": 0.2,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.0\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"counter\": 1,\n" +
                "    \"v_time\": 16,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.5,\n" +
                "      \"anger\": 0.5,\n" +
                "      \"disgust\": 0.0,\n" +
                "      \"fear\": 0.0,\n" +
                "      \"sadness\": 0.0,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.0\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"counter\": 1,\n" +
                "    \"v_time\": 31,\n" +
                "    \"u_time\": 1538553545148,\n" +
                "    \"genders\": {\n" +
                "      \"female\": 1\n" +
                "    },\n" +
                "    \"emotions\": {\n" +
                "      \"happiness\": 0.2,\n" +
                "      \"anger\": 0.2,\n" +
                "      \"disgust\": 0.2,\n" +
                "      \"fear\": 0.2,\n" +
                "      \"sadness\": 0.2,\n" +
                "      \"rested\": 0.0,\n" +
                "      \"surprise\": 0.0\n" +
                "    },\n" +
                "    \"source\": \"www.pippo.com\",\n" +
                "    \"url_id\": \"pippo\",\n" +
                "    \"ages\": {\n" +
                "      \"35\": 1\n" +
                "    }\n" +
                "  }\n" +
                "]\n"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        return if (BuildConfig.DEBUG) {
            Response.Builder()
                    .code(200)
                    .message(FAKE_RESPONSE)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), FAKE_RESPONSE.toByteArray()))
                    .addHeader("content-type", "application/json")
                    .build()
        } else {
            chain.proceed(chain.request())
        }
    }
}