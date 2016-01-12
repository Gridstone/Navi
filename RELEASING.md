Releasing
=========

 1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
 2. Update the `CHANGELOG.md` for the impending release.
 3. Update the `README.md` with the new version.
 4. `git commit -am "Prepare for release X.Y."` (where X.Y is the new version)
 5. `git tag -a X.Y -m "Version X.Y."` (where X.Y is the new version)
 6. `./gradlew clean build bintrayUpload -PdryRun=false -x mavenAndroidJavadocs`
 7. Update the `gradle.properties` to the next SNAPSHOT version.
 8. `git commit -am "Prepare the next development version."`
 9. `git push && git push --tags`
 10. Visit [Bintray](https://bintray.com/gridstone/Navi/navi/view) and publish the artifacts.