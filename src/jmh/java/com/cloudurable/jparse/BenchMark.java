package com.cloudurable.jparse;

import com.cloudurable.jparse.parser.JsonEventParser;
import com.cloudurable.jparse.parser.JsonParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.TokenEventListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import org.noggit.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class BenchMark {


    final static String jsonData;
    final static String doublesJsonData;
    final static String intsJsonData;
    final static String webXmlJsonData;
    final static String glossaryJsonData;




    final static TypeReference<HashMap<String, Object>> mapTypeRef
            = new TypeReference<>() {
    };


    final static TypeReference<List<Object>> listObjects
            = new TypeReference<>() {
    };

    final static TypeReference<List<Double>> listDoubleTypeRef
            = new TypeReference<>() {
    };

    final static TypeReference<List<BigDecimal>> listBigDTypeRef
            = new TypeReference<>() {
    };

    final static TypeReference<List<Float>> listFloatTypeRef
            = new TypeReference<>() {
    };


    final static ObjectMapper mapper = new ObjectMapper();

    final static String glossaryObjectPath = "glossary.GlossDiv.GlossList.GlossEntry.GlossDef.para";
    final static String webXmlObjectPath = "['web-app'].servlet[0]['init-param'].useJSP";

    //final static String objectPath = "1/1";


    static {
        try {
            //final File file = new File("./src/test/resources/json/glossary.json");
            //final File file = new File("./src/test/resources/json/doubles.json");
            //final File file = new File("./src/test/resources/json/ints.json");

            final File file = new File("./src/test/resources/json/webxml.json");
            //final File file = new File("./src/test/resources/json/types.json");


            intsJsonData = Sources.fileSource(new File("./src/test/resources/json/ints.json")).toString().trim();
            doublesJsonData = Sources.fileSource(new File("./src/test/resources/json/doubles.json")).toString().trim();
            glossaryJsonData = Sources.fileSource(new File("./src/test/resources/json/glossary.json")).toString().trim();
            webXmlJsonData = Sources.fileSource(new File("./src/test/resources/json/webxml.json")).toString().trim();
            jsonData = webXmlJsonData;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static void main(String... args) throws Exception {



        try {
            long startTime = System.currentTimeMillis();


            for (int i = 0; i < 1_500_000; i++) {

//                final RootNode root = new JsonParser().parse(webXmlJsonData);
//                final var result = Path.atPath(webXmlObjectPath, root);

                final var result = new JsonEventParser().parse(glossaryJsonData);

                //PathNode pathElements = Path.toPath("foo.bar.baz[99][0][10][11]['hi mom']");


                if (i % 100_000 == 0) {
                    System.out.printf("Elapsed time %s %s \n", ((System.currentTimeMillis() - startTime) / 1000.0), result);
                }
            }
            System.out.println("Total Elapsed time " + ((System.currentTimeMillis() - startTime) / 1000.0));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//
//
//    @Benchmark
//    public void readWebJSONJackson(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(jsonData, mapTypeRef));
//    }
//

//
//    @Benchmark
//    public void readWebJSONNoggitStax(Blackhole bh) throws Exception {
//
//        final var jsonParser =  new JSONParser(jsonData);
//
//        int event = -1;
//        while (event!=JSONParser.EOF) {
//            event = jsonParser.nextEvent();
//        }
//
//        bh.consume(event);
//    }
//    @Benchmark
//    public void readWebJSONNoggitObjectBuilder(Blackhole bh) throws Exception {
//
//        bh.consume(ObjectBuilder.fromJSON(jsonData));
//    }
//

//
//    @Benchmark
//    public void readWebJSONNoggitStax(Blackhole bh) throws Exception {
//
//        final var jsonParser =  new JSONParser(jsonData);
//
//        int event = -1;
//        while (event!=JSONParser.EOF) {
//            event = jsonParser.nextEvent();
//        }
//
//        bh.consume(event);
//    }
//    @Benchmark
//    public void readWebJSONJParse(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData));
//    }
//

//    @Benchmark
//    public void readWebJSONANoggitObjectBuilder(Blackhole bh) throws Exception {
//
//        bh.consume(ObjectBuilder.fromJSON(jsonData));
//    }


//    @Benchmark
//    public void readWebJSONJParse(Blackhole bh) {
//        bh.consume(new JsonParser().parse(webXmlJsonData));
//    }
//
//    @Benchmark
//    public void readWebJSONJackson(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(webXmlJsonData, mapTypeRef));
//    }

    @Benchmark
    public void readGlossaryJackson(Blackhole bh) throws JsonProcessingException {
        bh.consume(mapper.readValue(glossaryJsonData, mapTypeRef));
    }

    @Benchmark
    public void readGlossaryJParse(Blackhole bh) {
        bh.consume(new JsonParser().parse(glossaryJsonData));
    }

    @Benchmark
    public void readGlossaryJParseWithEvents(Blackhole bh) {
        bh.consume(new JsonEventParser().parse(glossaryJsonData));
    }

    @Benchmark
    public void readGlossaryNoggitEvent(Blackhole bh) throws Exception {

        final var jsonParser =  new JSONParser(glossaryJsonData);

        int event = -1;
        while (event!=JSONParser.EOF) {
            event = jsonParser.nextEvent();
        }

        bh.consume(event);
    }


    @Benchmark
    public void readGlossaryEventJParse(Blackhole bh) throws Exception {

        final var jsonParser =  new JsonEventParser();
        final int [] token = new int[1];
        final var events = new TokenEventListener() {
            @Override
            public void start(int tokenId, int index, CharSource source) {
                token[0] = tokenId;
            }

            @Override
            public void end(int tokenId, int index, CharSource source) {
                token[0] = tokenId;
            }
        };

        jsonParser.parse(glossaryJsonData, events);

        bh.consume(token);
    }


//
//    @Benchmark
//    public void jParseFloatArray(Blackhole bh) {
//        bh.consume(Json.toArrayNode(doublesJsonData).getFloatArray());
//    }
//
//    @Benchmark
//    public void jParseFloatArrayFast(Blackhole bh) {
//        bh.consume(Json.toArrayNode(doublesJsonData).getFloatArrayFast());
//    }

//    @Benchmark
//    public void jParseDoubleArray(Blackhole bh) {
//        bh.consume(Json.toArrayNode(doublesJsonData).getDoubleArray());
//    }
//
//    @Benchmark
//    public void jParseDoubleArrayFast(Blackhole bh) {
//        bh.consume(Json.toArrayNode(doublesJsonData).getDoubleArrayFast());
//    }
//    @Benchmark
//    public void readWebGlossaryNoggitObjectBuilder(Blackhole bh) throws Exception {
//
//        bh.consume(ObjectBuilder.fromJSON(glossaryJsonData));
//    }



//    @Benchmark
//    public void simpleFullBigDeserializeJJson(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).asCompleteObjectBig());
//    }
//
//    @Benchmark
//    public void simpleFullMediumDeserializeJJson(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).asCompleteObject());
//    }

//    @Benchmark
//    public void simpleFullSmallDeserializeJJson(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).asCompleteObjectSmall());
//    }

//    @Benchmark
//    public void simpleFullSmallDeserializeJJsonThenWalk(Blackhole bh) {
//        bh.consume(TestUtils.walkFull(new JsonParser().parse(jsonData).asCompleteObjectSmall()));
//    }
//
//    @Benchmark
//    public void simpleDeserializeJacksonThenWalk(Blackhole bh) throws JsonProcessingException {
//        bh.consume(TestUtils.walkFull(mapper.readValue(jsonData, mapTypeRef)));
//    }




//    @Benchmark
//    public void pathParse(Blackhole bh) {
//      bh.consume(Path.toPath("foo.bar.baz[99][0][10][11]['hi mom']"));
//    }
//
//    @Benchmark
//    public void jParseLongArrayFast(Blackhole bh) {
//        bh.consume(Json.toArrayNode(intsJsonData).getLongArrayFast());
//    }
//
//
//    @Benchmark
//    public void jParseLongArray(Blackhole bh) {
//        bh.consume(Json.toArrayNode(intsJsonData).getLongArray());
//    }
//
//    @Benchmark
//    public void jacksonLongArray(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(intsJsonData, long[].class));
//    }
//
//    @Benchmark
//    public void jParseIntArray(Blackhole bh) {
//        bh.consume(Json.toArrayNode(intsJsonData).getIntArray());
//    }
//
//    @Benchmark
//    public void jParseIntArrayFast(Blackhole bh) {
//        bh.consume(Json.toArrayNode(intsJsonData).getIntArrayFast());
//    }
//
//    @Benchmark
//    public void jacksonIntArray(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(intsJsonData, int[].class));
//    }
//
//
//    @Benchmark
//    public void jParseBigIntArray(Blackhole bh) {
//        bh.consume(Json.toArrayNode(intsJsonData).getBigIntegerArray());
//
//    }
//
//    @Benchmark
//    public void jacksonBigIntArray(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(intsJsonData, BigInteger[].class));
//    }


//
//
//    @Benchmark
//    public void jacksonFloatArray(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(doublesJsonData, float[].class));
//    }

//

//
//
//    @Benchmark
//    public void jacksonDoubleArray(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(doublesJsonData, double[].class));
//    }
//
//
//    @Benchmark
//    public void jParseBigDecimalArray(Blackhole bh) {
//        bh.consume(Json.toArrayNode(doublesJsonData).getBigDecimalArray());
//    }
//
//
//    @Benchmark
//    public void jacksonBigDecimalArray(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(doublesJsonData, BigDecimal[].class));
//    }


//
//    @Benchmark
//    public void simpleDeserializeJacksonWebXML(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(jsonData, mapTypeRef));
//    }
//
//
//    @Benchmark
//    public void simpleDeserializeJJsonWebXML(Blackhole bh) {
//
//        final JsonParser jsonParser = new JsonParser();
//
//        bh.consume(jsonParser.parse(jsonData));
//    }

//        @Benchmark
//    public void simpleDeserializeJacksonWebXMLTenTimes(Blackhole bh) throws JsonProcessingException {
//        for (int i = 0; i < 100; i++)
//        bh.consume(mapper.readValue(jsonData, mapTypeRef));
//    }
//
//
//    @Benchmark
//    public void simpleDeserializeJJsonWebXMLTenTimes(Blackhole bh) {
//
//        final JsonParser jsonParser = new JsonParser();
//
//        for (int i = 0; i < 100; i++)
//        bh.consume(jsonParser.parse(jsonData));
//    }
//
//    @Benchmark
//    public void jaywayThenPathGrabGlossary(Blackhole bh) throws JsonProcessingException {
//
//        DocumentContext jsonContext = JsonPath.parse(glossaryJsonData);
//        String result = jsonContext.read(glossaryObjectPath);
//        bh.consume(result);
//    }
//    @Benchmark
//    public void jparsePathGrabGlossary(Blackhole bh) {
//        bh.consume(Path.atPath(glossaryObjectPath, new JsonParser().parse(glossaryJsonData)));
//    }
//
//    @Benchmark
//    public void jaywayPathGrabWebXML(Blackhole bh) throws JsonProcessingException {
//
//        DocumentContext jsonContext = JsonPath.parse(webXmlJsonData);
//        Boolean result = jsonContext.read(webXmlObjectPath);
//        bh.consume(result);
//    }
//
//    @Benchmark
//    public void jparsePathGrabWebXML(Blackhole bh) {
//        bh.consume(Path.atPath(webXmlObjectPath, new JsonParser().parse(webXmlJsonData)));
//    }
//
//
//    @Benchmark
//    public void jacksonPathGrabWebXML(Blackhole bh) throws JsonProcessingException {
//        bh.consume(PathUtils.getLastObject(webXmlObjectPath, mapper.readValue(webXmlJsonData, mapTypeRef)));
//    }
//
//    @Benchmark
//    public void jacksonPathGrabGlossary(Blackhole bh) throws JsonProcessingException {
//        bh.consume(PathUtils.getLastObject(glossaryObjectPath, mapper.readValue(glossaryJsonData, mapTypeRef)));
//    }


//    @Benchmark
//    public void deserializeIntoMapJParse(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData));
//    }
//
//    @Benchmark
//    public void deserializeIntoMapJackson(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(jsonData, mapTypeRef));
//    }
//
//    @Benchmark
//    public void deserializeIntoMapJParseAndGetPathThenSerialize(Blackhole bh) {
//        final var map = Json.toMap(glossaryJsonData);
//        final var map2 = map.get("glossary");
//        final var map3 = ((Map<String, Object>) map2).get("GlossDiv");
//        bh.consume(map);
//        bh.consume(Json.serialize(map3));
//    }
//
//    @Benchmark
//    public void deserializeIntoMapJacksonAndGetPathThenSerialize(Blackhole bh) throws JsonProcessingException {
//        final var map = mapper.readValue(glossaryJsonData, mapTypeRef);
//        final var map2 = map.get("glossary");
//        final var map3 = ((Map<String, Object>) map2).get("GlossDiv");
//        bh.consume(map);
//        bh.consume(mapper.writeValueAsString(map3));
//    }


//    @Benchmark
//    public void deserializeIntoMapJParseAndSerialize(Blackhole bh) {
//        final var map = Json.toMap(jsonData);
//        bh.consume(Json.serialize(map));
//    }
//
//    @Benchmark
//    public void deserializeIntoMapJacksonAndSerialize(Blackhole bh) throws JsonProcessingException {
//        final var map = mapper.readValue(jsonData, mapTypeRef);
//        bh.consume(mapper.writeValueAsString(map));
//    }

//    @Benchmark
//    public void simpleParseJackson(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readTree(jsonData));
//    }


//    @Benchmark
//    public void simpleDeserializeJJsonFull(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).getString().toString());
//    }
//
//    @Benchmark
//    public void simpleDeserializeJJsonFullEncoded(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).getString().toEncodedString());
//    }


//    @Benchmark
//    public void simpleDeserializeJacksonString(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(jsonData, String.class));
//    }
//    @Benchmark
//    public void simpleDeserializeJacksonDoubleList(Blackhole bh) throws JsonProcessingException {
//        //bh.consume(mapper.readValue(jsonData, mapTypeRef));
//        bh.consume(mapper.readValue(jsonData, listDoubleTypeRef));
//    }
//
//    @Benchmark
//    public void simpleDeserializeJacksonFloatList(Blackhole bh) throws JsonProcessingException {
//        //bh.consume(mapper.readValue(jsonData, mapTypeRef));
//        bh.consume(mapper.readValue(jsonData, listFloatTypeRef));
//    }
//    @Benchmark
//    public void simpleDeserializeJacksonBigDecimalList(Blackhole bh) throws JsonProcessingException {
//        bh.consume(mapper.readValue(jsonData, listBigDTypeRef));
//    }
//
//
//    @Benchmark
//    public void simpleFullBigDeserializeJJson(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).asCompleteObjectBig());
//    }
//
//    @Benchmark
//    public void simpleFullMediumDeserializeJJson(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).asCompleteObject());
//    }
//
//    @Benchmark
//    public void simpleFullSmallDeserializeJJson(Blackhole bh) {
//        bh.consume(new JsonParser().parse(jsonData).asCompleteObjectSmall());
//    }
}
