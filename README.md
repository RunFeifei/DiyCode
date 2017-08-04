###keystore
MD5: B8:03:22:EE:E9:72:02:98:72:AD:C9:FA:C4:9B:F3:34
SHA1: 86:BE:52:48:CB:00:13:48:AD:CB:23:F3:94:E4:C8:26:A9:CD:93:79
SHA256: 9E:0B:28:66:E3:05:85:72:B9:86:95:9A:B0:91:FD:96:00:C2:CD:9F:32:36:0F:15:E5:98:EB:5C:F6:3E:79:A1

key.store=keystorePath
key.alias=android.keystore
key.store.password=dianrong
key.alias.password=dianrong  
./gradlew clean build bintrayUpload -PbintrayUser=feifei -PbintrayKey=69175aa7101760b64a7fee5aea872d4d84d14c8c -PdryRun=false