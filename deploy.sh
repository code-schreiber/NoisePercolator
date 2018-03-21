#!/bin/bash

EXPECTED_TRAVIS_REPO_SLUG="code-schreiber/NoisePercolator"
EXPECTED_TRAVIS_BRANCH="master"

# Script should bail on first error
set -e

echo "Running deploy.sh"
echo "deploy.sh: TRAVIS_BRANCH: $TRAVIS_BRANCH"
echo "deploy.sh: TRAVIS_REPO_SLUG: $TRAVIS_REPO_SLUG"
echo "deploy.sh: TRAVIS_TAG: $TRAVIS_TAG"
echo "deploy.sh: TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"

echo "deploy.sh: Running gradle build"
./gradlew build
echo "deploy.sh: Creating emulator"
echo no | android create avd -n emulatorApi26 -k "system-images;android-26;x86"
echo "deploy.sh: Starting emulator"
emulator -avd emulatorApi26 -no-audio -no-window &
echo "deploy.sh: Waiting for emulator to be ready"
android-wait-for-emulator
echo "deploy.sh: Running gradle connectedAndroidTest"
./gradlew connectedAndroidTest
echo "deploy.sh: app/build/outputs/apk/release now contains:"
ls -l app/build/outputs/apk/release
#echo "deploy.sh: Running gradle printStatsFromThisVersion"
#./gradlew printStatsFromThisVersion
#echo "deploy.sh: Running gradle printVersion"
#./gradlew printVersion

if [ "$TRAVIS_REPO_SLUG" != "$EXPECTED_TRAVIS_REPO_SLUG" ]; then
  echo "deploy.sh: Skipping deployment: wrong repository. Expected '$EXPECTED_TRAVIS_REPO_SLUG' but was '$TRAVIS_REPO_SLUG'."
elif [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  echo "deploy.sh: Skipping deployment: was pull request."
elif [ "$TRAVIS_BRANCH" != "$EXPECTED_TRAVIS_BRANCH" ]; then
  echo "deploy.sh: Skipping deployment: wrong branch. Expected '$EXPECTED_TRAVIS_BRANCH' but was '$TRAVIS_BRANCH'."
else
  echo "deploy.sh: Deploying to Google Play"
#  echo "deploy.sh: Running gradle firebaseUploadReleaseProguardMapping"
#  ./gradlew firebaseUploadReleaseProguardMapping
#  cd ..
  echo "deploy.sh: Running fastlane supply"
  fastlane supply --version
  fastlane supply run --json_key dev-console-api-private-key.json --package_name com.toolslab.noisepercolator --apk app/build/outputs/apk/release/app-release.apk --track alpha
  echo "deploy.sh: Deployed to Google Play"
  echo "Thank you, come again."
  exit $?
fi
echo "Exiting deploy.sh, skipping deployment"
