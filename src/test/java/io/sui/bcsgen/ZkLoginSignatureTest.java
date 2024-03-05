package io.sui.bcsgen;

import com.google.common.primitives.Bytes;
import io.sui.Sui;
import io.sui.crypto.SignatureScheme;
import io.sui.models.enoki.IssBase64Details;
import io.sui.models.enoki.ProofPoints;
import io.sui.models.enoki.ZkProofResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

class ZkLoginSignatureTest {

    private ZkLoginSignature zkLoginSignature;
    private ZkLoginSignature zkLoginSignature2;

    private ZkProofResponse zkProofResponse;


    @BeforeEach
    public void setup() {
        ZkProofPoints zkProofPoints = new ZkProofPoints(
                List.of(
                    "8184706123831149040236686809664999399435669631477734613894797406744981249483",
                    "15015878115291738707351427464601803298331484414346324048750049898836961855378",
                    "1"
                ),
                List.of(
                    List.of(
                        "15325081913230693654620477499425771953565756979471563192669461005135063028765",
                        "11417186234779272759429280808668455463669666742412119165740112711610353418578"
                    ),
                    List.of(
                        "17576691694593051533421311276532906193101981445275975349140960330080558605477",
                        "18904986392703267153364484678271692255449171078332926100018346447747092135368"
                    ),
                    List.of(
                        "1", "0"
                    )
                ),
                List.of(
                    "19259813299435434088010419172365623889571283632024773483365717581426070826732",
                    "17807243709957976676300264208804646113343190684852815134901815300937473948959",
                    "1"
                )
        );
        ZkIssBase64Details zkIssBase64Details = new ZkIssBase64Details("yJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLC", 1);
        String headerBase64 = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg1ZTU1MTA3NDY2YjdlMjk4MzYxOTljNThjNzU4MWY1YjkyM2JlNDQiLCJ0eXAiOiJKV1QifQ";
        String addressSeed = "18731939608506791929702681480829865117478261398604016119762145446331446154420";
        ZkProof zkProof = new ZkProof(zkProofPoints, zkIssBase64Details, headerBase64, addressSeed);
        String maxEpoch = "293";
        List<Byte> userSignature = java.util.Arrays.asList(
                ArrayUtils.toObject(
                        java.util.Base64.getDecoder().decode(
                                "AGFw2hSMHakAEiT1R7ajkcE6rz0ixZckffsFiWKZ8dSSlBgyyPoh2RW7YNLXxjDfYm/QOKy3BmEcRJNTvXDXagaFJN4IlaSHWOdi4/wvc9kXlK3x1mpLcknOoTnGJQuoVQ=="
        )));
        ZkLoginSignatureData zkLoginSignatureData = new ZkLoginSignatureData(
                zkProof, maxEpoch, userSignature
        );
        zkLoginSignature = new ZkLoginSignature(zkLoginSignatureData);

        ZkProofPoints zkProofPoints2 = new ZkProofPoints(
                List.of(
                        "11701866812704517213914612798674748657755566586597434810941240483346769369267",
                        "14120438998063692297386249754230972715153876537530168883981513584586172195841",
                        "1"
                ),
                List.of(
                        List.of(
                                "1867454501602583848852787782761996560170118249299507014999230886852556998273",
                                "14466419698679116313475210654562949128349597101769256959010003188886091080310"
                        ),
                        List.of(
                                "11072954562924588496148632474078432406633288948256718809714861986119303529760",
                                "19790516010784935100150614989097908233899718645536130727773713407611161368046"
                        ),
                        List.of(
                                "1", "0"
                        )
                ),
                List.of(
                        "10423289051853033915380810516130205747182867596457139999176714476415148376350",
                        "21785719695848013908061492989765777788142255897986300241561280196745174934457",
                        "1"
                )
        );
        ZkIssBase64Details zkIssBase64Details2 = new ZkIssBase64Details("yJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLC", 1);
        String headerBase642 = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImI5YWM2MDFkMTMxZmQ0ZmZkNTU2ZmYwMzJhYWIxODg4ODBjZGUzYjkiLCJ0eXAiOiJKV1QifQ";
        String addressSeed2 = "13322897930163218532266430409510394316985274769125667290600321564259466511711";
        ZkProof zkProof2 = new ZkProof(zkProofPoints2, zkIssBase64Details2, headerBase642, addressSeed2);
        String maxEpoch2 = "174";
        List<Byte> userSignature2 = java.util.Arrays.asList(
                ArrayUtils.toObject(
                        java.util.Base64.getDecoder().decode(
                                "AEp+O5GEAF/5tKNDdWBObNf/1uIrbOJmE+xpnlBD2Vikqhbd0zLrQ2NJyquYXp4KrvWUOl7Hso+OK0eiV97ffwucM8VdtG2hjf/RUGNO5JNUH+D/gHtE9sHe6ZEnxwZL7g=="
                        )));
        ZkLoginSignatureData zkLoginSignatureData2 = new ZkLoginSignatureData(
                zkProof2, maxEpoch2, userSignature2
        );
        zkLoginSignature2 = new ZkLoginSignature(zkLoginSignatureData2);

        zkProofResponse = new ZkProofResponse(
                new ProofPoints(List.of(
                        "10470600401800762879997003571136011468264740152960662084353691861775821402317",
                        "3536220572364653787829037451150555941432248566811846242076906860880030444856",
                        "1"
                ),
                        List.of(
                                List.of(
                                        "9418137863720099986355930009029494522457351443514381114712338661637118634333",
                                        "2731111556269572359376239974641017954179485035762370851187579394976878790670"
                                ),
                                List.of(
                                        "15437591463409089806906428900208783083887283903928479240333742756957637621854",
                                        "6688713650821857399245012487845871723979022040466067178007751248197340860686"
                                ),
                                List.of(
                                        "1", "0"
                                )
                        ),
                        List.of(
                                "3534144322688475517574077183882355330913543114555714000518879628811844663150",
                                "1860077389203675543312635032112444952403307708276806264192431187327449835818",
                                "1"
                        )),
                new IssBase64Details("yJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLC", 1),
                headerBase64, addressSeed);

    }

    @Test
    void testBcsSerialization() throws Exception {
        byte[] bytes = zkLoginSignature.bcsSerialize();
        byte[] flaggedSignature = Bytes.concat(new byte[] {SignatureScheme.ZkLogin.getScheme()}, bytes);
        System.out.println(Base64.toBase64String(flaggedSignature));
        System.out.println(new Sui("http://localhost", "http://localhost", "/tmp/asd.keystore", "http://localhost", "apikey")
                .getZkLoginSignature(zkProofResponse, "293", "ADRL2OIbWLztdzcNzDQWG9bhfdwZFm62sYMSWbAI6ZWrQhG5XE9tKbxO9tNDBOM9PSkTksXSLSxXRIxTyh1SPQ2nBDwZLep9OpbIGrUz/QhUdlAwh2/rU7ngI93TaRL+IQ=="));
    }

    @Test
    void testBcsDeSerialization() throws Exception {
//        byte[] bytes = zkLoginSignature2.bcsSerialize();
//        String sig = "BQNNMTE3MDE4NjY4MTI3MDQ1MTcyMTM5MTQ2MTI3OTg2NzQ3NDg2NTc3NTU1NjY1ODY1OTc0MzQ4MTA5NDEyNDA0ODMzNDY3NjkzNjkyNjdNMTQxMjA0Mzg5OTgwNjM2OTIyOTczODYyNDk3NTQyMzA5NzI3MTUxNTM4NzY1Mzc1MzAxNjg4ODM5ODE1MTM1ODQ1ODYxNzIxOTU4NDEBMQMCTDE4Njc0NTQ1MDE2MDI1ODM4NDg4NTI3ODc3ODI3NjE5OTY1NjAxNzAxMTgyNDkyOTk1MDcwMTQ5OTkyMzA4ODY4NTI1NTY5OTgyNzNNMTQ0NjY0MTk2OTg2NzkxMTYzMTM0NzUyMTA2NTQ1NjI5NDkxMjgzNDk1OTcxMDE3NjkyNTY5NTkwMTAwMDMxODg4ODYwOTEwODAzMTACTTExMDcyOTU0NTYyOTI0NTg4NDk2MTQ4NjMyNDc0MDc4NDMyNDA2NjMzMjg4OTQ4MjU2NzE4ODA5NzE0ODYxOTg2MTE5MzAzNTI5NzYwTTE5NzkwNTE2MDEwNzg0OTM1MTAwMTUwNjE0OTg5MDk3OTA4MjMzODk5NzE4NjQ1NTM2MTMwNzI3NzczNzEzNDA3NjExMTYxMzY4MDQ2AgExATADTTEwNDIzMjg5MDUxODUzMDMzOTE1MzgwODEwNTE2MTMwMjA1NzQ3MTgyODY3NTk2NDU3MTM5OTk5MTc2NzE0NDc2NDE1MTQ4Mzc2MzUwTTIxNzg1NzE5Njk1ODQ4MDEzOTA4MDYxNDkyOTg5NzY1Nzc3Nzg4MTQyMjU1ODk3OTg2MzAwMjQxNTYxMjgwMTk2NzQ1MTc0OTM0NDU3ATExeUpwYzNNaU9pSm9kSFJ3Y3pvdkwyRmpZMjkxYm5SekxtZHZiMmRzWlM1amIyMGlMQwEAAABmZXlKaGJHY2lPaUpTVXpJMU5pSXNJbXRwWkNJNkltSTVZV00yTURGa01UTXhabVEwWm1aa05UVTJabVl3TXpKaFlXSXhPRGc0T0RCalpHVXpZamtpTENKMGVYQWlPaUpLVjFRaWZRTTEzMzIyODk3OTMwMTYzMjE4NTMyMjY2NDMwNDA5NTEwMzk0MzE2OTg1Mjc0NzY5MTI1NjY3MjkwNjAwMzIxNTY0MjU5NDY2NTExNzExAzE3NGEASn47kYQAX/m0o0N1YE5s1//W4its4mYT7GmeUEPZWKSqFt3TMutDY0nKq5hengqu9ZQ6Xseyj44rR6JX3t9/C5wzxV20baGN/9FQY07kk1Qf4P+Ae0T2wd7pkSfHBkvu";
//        String sig = "BQNNMTE3MDE4NjY4MTI3MDQ1MTcyMTM5MTQ2MTI3OTg2NzQ3NDg2NTc3NTU1NjY1ODY1OTc0MzQ4MTA5NDEyNDA0ODMzNDY3NjkzNjkyNjdNMTQxMjA0Mzg5OTgwNjM2OTIyOTczODYyNDk3NTQyMzA5NzI3MTUxNTM4NzY1Mzc1MzAxNjg4ODM5ODE1MTM1ODQ1ODYxNzIxOTU4NDEBMQMCTDE4Njc0NTQ1MDE2MDI1ODM4NDg4NTI3ODc3ODI3NjE5OTY1NjAxNzAxMTgyNDkyOTk1MDcwMTQ5OTkyMzA4ODY4NTI1NTY5OTgyNzNNMTQ0NjY0MTk2OTg2NzkxMTYzMTM0NzUyMTA2NTQ1NjI5NDkxMjgzNDk1OTcxMDE3NjkyNTY5NTkwMTAwMDMxODg4ODYwOTEwODAzMTACTTExMDcyOTU0NTYyOTI0NTg4NDk2MTQ4NjMyNDc0MDc4NDMyNDA2NjMzMjg4OTQ4MjU2NzE4ODA5NzE0ODYxOTg2MTE5MzAzNTI5NzYwTTE5NzkwNTE2MDEwNzg0OTM1MTAwMTUwNjE0OTg5MDk3OTA4MjMzODk5NzE4NjQ1NTM2MTMwNzI3NzczNzEzNDA3NjExMTYxMzY4MDQ2AgExATADTTEwNDIzMjg5MDUxODUzMDMzOTE1MzgwODEwNTE2MTMwMjA1NzQ3MTgyODY3NTk2NDU3MTM5OTk5MTc2NzE0NDc2NDE1MTQ4Mzc2MzUwTTIxNzg1NzE5Njk1ODQ4MDEzOTA4MDYxNDkyOTg5NzY1Nzc3Nzg4MTQyMjU1ODk3OTg2MzAwMjQxNTYxMjgwMTk2NzQ1MTc0OTM0NDU3ATExeUpwYzNNaU9pSm9kSFJ3Y3pvdkwyRmpZMjkxYm5SekxtZHZiMmRzWlM1amIyMGlMQwFmZXlKaGJHY2lPaUpTVXpJMU5pSXNJbXRwWkNJNkltSTVZV00yTURGa01UTXhabVEwWm1aa05UVTJabVl3TXpKaFlXSXhPRGc0T0RCalpHVXpZamtpTENKMGVYQWlPaUpLVjFRaWZRTTEzMzIyODk3OTMwMTYzMjE4NTMyMjY2NDMwNDA5NTEwMzk0MzE2OTg1Mjc0NzY5MTI1NjY3MjkwNjAwMzIxNTY0MjU5NDY2NTExNzExrgAAAAAAAABhAEp+O5GEAF/5tKNDdWBObNf/1uIrbOJmE+xpnlBD2Vikqhbd0zLrQ2NJyquYXp4KrvWUOl7Hso+OK0eiV97ffwucM8VdtG2hjf/RUGNO5JNUH+D/gHtE9sHe6ZEnxwZL7g==";
//        String sig = "BQNNMTA0NzA2MDA0MDE4MDA3NjI4Nzk5OTcwMDM1NzExMzYwMTE0NjgyNjQ3NDAxNTI5NjA2NjIwODQzNTM2OTE4NjE3NzU4MjE0MDIzMTdMMzUzNjIyMDU3MjM2NDY1Mzc4NzgyOTAzNzQ1MTE1MDU1NTk0MTQzMjI0ODU2NjgxMTg0NjI0MjA3NjkwNjg2MDg4MDAzMDQ0NDg1NgExAwJMOTQxODEzNzg2MzcyMDA5OTk4NjM1NTkzMDAwOTAyOTQ5NDUyMjQ1NzM1MTQ0MzUxNDM4MTExNDcxMjMzODY2MTYzNzExODYzNDMzM0wyNzMxMTExNTU2MjY5NTcyMzU5Mzc2MjM5OTc0NjQxMDE3OTU0MTc5NDg1MDM1NzYyMzcwODUxMTg3NTc5Mzk0OTc2ODc4NzkwNjcwAk0xNTQzNzU5MTQ2MzQwOTA4OTgwNjkwNjQyODkwMDIwODc4MzA4Mzg4NzI4MzkwMzkyODQ3OTI0MDMzMzc0Mjc1Njk1NzYzNzYyMTg1NEw2Njg4NzEzNjUwODIxODU3Mzk5MjQ1MDEyNDg3ODQ1ODcxNzIzOTc5MDIyMDQwNDY2MDY3MTc4MDA3NzUxMjQ4MTk3MzQwODYwNjg2AgExATADTDM1MzQxNDQzMjI2ODg0NzU1MTc1NzQwNzcxODM4ODIzNTUzMzA5MTM1NDMxMTQ1NTU3MTQwMDA1MTg4Nzk2Mjg4MTE4NDQ2NjMxNTBMMTg2MDA3NzM4OTIwMzY3NTU0MzMxMjYzNTAzMjExMjQ0NDk1MjQwMzMwNzcwODI3NjgwNjI2NDE5MjQzMTE4NzMyNzQ0OTgzNTgxOAExMXlKcGMzTWlPaUpvZEhSd2N6b3ZMMkZqWTI5MWJuUnpMbWR2YjJkc1pTNWpiMjBpTEMBAAAAZmV5SmhiR2NpT2lKU1V6STFOaUlzSW10cFpDSTZJamcxWlRVMU1UQTNORFkyWWpkbE1qazRNell4T1Rsak5UaGpOelU0TVdZMVlqa3lNMkpsTkRRaUxDSjBlWEFpT2lKS1YxUWlmUU0xODczMTkzOTYwODUwNjc5MTkyOTcwMjY4MTQ4MDgyOTg2NTExNzQ3ODI2MTM5ODYwNDAxNjExOTc2MjE0NTQ0NjMzMTQ0NjE1NDQyMAMyOTNhADRL2OIbWLztdzcNzDQWG9bhfdwZFm62sYMSWbAI6ZWrQhG5XE9tKbxO9tNDBOM9PSkTksXSLSxXRIxTyh1SPQ2nBDwZLep9OpbIGrUz/QhUdlAwh2/rU7ngI93TaRL+IQ==";
//        String sig = "BQNNMTA0NzA2MDA0MDE4MDA3NjI4Nzk5OTcwMDM1NzExMzYwMTE0NjgyNjQ3NDAxNTI5NjA2NjIwODQzNTM2OTE4NjE3NzU4MjE0MDIzMTdMMzUzNjIyMDU3MjM2NDY1Mzc4NzgyOTAzNzQ1MTE1MDU1NTk0MTQzMjI0ODU2NjgxMTg0NjI0MjA3NjkwNjg2MDg4MDAzMDQ0NDg1NgExAwJMOTQxODEzNzg2MzcyMDA5OTk4NjM1NTkzMDAwOTAyOTQ5NDUyMjQ1NzM1MTQ0MzUxNDM4MTExNDcxMjMzODY2MTYzNzExODYzNDMzM0wyNzMxMTExNTU2MjY5NTcyMzU5Mzc2MjM5OTc0NjQxMDE3OTU0MTc5NDg1MDM1NzYyMzcwODUxMTg3NTc5Mzk0OTc2ODc4NzkwNjcwAk0xNTQzNzU5MTQ2MzQwOTA4OTgwNjkwNjQyODkwMDIwODc4MzA4Mzg4NzI4MzkwMzkyODQ3OTI0MDMzMzc0Mjc1Njk1NzYzNzYyMTg1NEw2Njg4NzEzNjUwODIxODU3Mzk5MjQ1MDEyNDg3ODQ1ODcxNzIzOTc5MDIyMDQwNDY2MDY3MTc4MDA3NzUxMjQ4MTk3MzQwODYwNjg2AgExATADTDM1MzQxNDQzMjI2ODg0NzU1MTc1NzQwNzcxODM4ODIzNTUzMzA5MTM1NDMxMTQ1NTU3MTQwMDA1MTg4Nzk2Mjg4MTE4NDQ2NjMxNTBMMTg2MDA3NzM4OTIwMzY3NTU0MzMxMjYzNTAzMjExMjQ0NDk1MjQwMzMwNzcwODI3NjgwNjI2NDE5MjQzMTE4NzMyNzQ0OTgzNTgxOAExMXlKcGMzTWlPaUpvZEhSd2N6b3ZMMkZqWTI5MWJuUnpMbWR2YjJkc1pTNWpiMjBpTEMBZmV5SmhiR2NpT2lKU1V6STFOaUlzSW10cFpDSTZJamcxWlRVMU1UQTNORFkyWWpkbE1qazRNell4T1Rsak5UaGpOelU0TVdZMVlqa3lNMkpsTkRRaUxDSjBlWEFpT2lKS1YxUWlmUU0xODczMTkzOTYwODUwNjc5MTkyOTcwMjY4MTQ4MDgyOTg2NTExNzQ3ODI2MTM5ODYwNDAxNjExOTc2MjE0NTQ0NjMzMTQ0NjE1NDQyMCUBAAAAAAAAYQA0S9jiG1i87Xc3Dcw0FhvW4X3cGRZutrGDElmwCOmVq0IRuVxPbSm8TvbTQwTjPT0pE5LF0i0sV0SMU8odUj0NpwQ8GS3qfTqWyBq1M/0IVHZQMIdv61O54CPd02kS/iE=";
        String sig = "BQNMODE4NDcwNjEyMzgzMTE0OTA0MDIzNjY4NjgwOTY2NDk5OTM5OTQzNTY2OTYzMTQ3NzczNDYxMzg5NDc5NzQwNjc0NDk4MTI0OTQ4M00xNTAxNTg3ODExNTI5MTczODcwNzM1MTQyNzQ2NDYwMTgwMzI5ODMzMTQ4NDQxNDM0NjMyNDA0ODc1MDA0OTg5ODgzNjk2MTg1NTM3OAExAwJNMTUzMjUwODE5MTMyMzA2OTM2NTQ2MjA0Nzc0OTk0MjU3NzE5NTM1NjU3NTY5Nzk0NzE1NjMxOTI2Njk0NjEwMDUxMzUwNjMwMjg3NjVNMTE0MTcxODYyMzQ3NzkyNzI3NTk0MjkyODA4MDg2Njg0NTU0NjM2Njk2NjY3NDI0MTIxMTkxNjU3NDAxMTI3MTE2MTAzNTM0MTg1NzgCTTE3NTc2NjkxNjk0NTkzMDUxNTMzNDIxMzExMjc2NTMyOTA2MTkzMTAxOTgxNDQ1Mjc1OTc1MzQ5MTQwOTYwMzMwMDgwNTU4NjA1NDc3TTE4OTA0OTg2MzkyNzAzMjY3MTUzMzY0NDg0Njc4MjcxNjkyMjU1NDQ5MTcxMDc4MzMyOTI2MTAwMDE4MzQ2NDQ3NzQ3MDkyMTM1MzY4AgExATADTTE5MjU5ODEzMjk5NDM1NDM0MDg4MDEwNDE5MTcyMzY1NjIzODg5NTcxMjgzNjMyMDI0NzczNDgzMzY1NzE3NTgxNDI2MDcwODI2NzMyTTE3ODA3MjQzNzA5OTU3OTc2Njc2MzAwMjY0MjA4ODA0NjQ2MTEzMzQzMTkwNjg0ODUyODE1MTM0OTAxODE1MzAwOTM3NDczOTQ4OTU5ATExeUpwYzNNaU9pSm9kSFJ3Y3pvdkwyRmpZMjkxYm5SekxtZHZiMmRzWlM1amIyMGlMQwFmZXlKaGJHY2lPaUpTVXpJMU5pSXNJbXRwWkNJNklqZzFaVFUxTVRBM05EWTJZamRsTWprNE16WXhPVGxqTlRoak56VTRNV1kxWWpreU0ySmxORFFpTENKMGVYQWlPaUpLVjFRaWZRTTE4NzMxOTM5NjA4NTA2NzkxOTI5NzAyNjgxNDgwODI5ODY1MTE3NDc4MjYxMzk4NjA0MDE2MTE5NzYyMTQ1NDQ2MzMxNDQ2MTU0NDIwJQEAAAAAAABhAGFw2hSMHakAEiT1R7ajkcE6rz0ixZckffsFiWKZ8dSSlBgyyPoh2RW7YNLXxjDfYm/QOKy3BmEcRJNTvXDXagaFJN4IlaSHWOdi4/wvc9kXlK3x1mpLcknOoTnGJQuoVQ==";
        byte[] bytes = Base64.decode(sig);
        ZkLoginSignature zkLoginSignature1 = ZkLoginSignature.bcsDeserialize(Arrays.copyOfRange(bytes, 1, bytes.length));
        List<@com.novi.serde.Unsigned Byte> list = zkLoginSignature1.value.userSignature;
        byte[] result = new byte[list.size()];
        for(int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).byteValue();
        }
        System.out.println(Base64.toBase64String(result));
    }
}