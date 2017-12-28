key.store=keystorePath
key.alias=fei.keystore
key.store.password=feifei
key.alias.password=feifei  
./gradlew clean build bintrayUpload -PbintrayUser=feifei -PbintrayKey=69175aa7101760b64a7fee5aea872d4d84d14c8c -PdryRun=false