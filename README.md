Master  
[![Build Status](https://travis-ci.org/code-schreiber/NoisePercolator.svg?branch=master)](https://travis-ci.org/code-schreiber/NoisePercolator)  
Develop  
[![Build Status](https://travis-ci.org/code-schreiber/NoisePercolator.svg?branch=develop)](https://travis-ci.org/code-schreiber/NoisePercolator)

<p align="center">
 <b>Noise Percolator</b>
 <br>
 <img src='https://github.com/code-schreiber/NoisePercolator/raw/master/metadata/en-US/images/icon.png' width='100' height='100'/>
 <br>
 <a href='https://play.google.com/store/apps/details?id=com.toolslab.noisepercolator&utm_source=github'>
  <img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width='200'/>
 </a>
</p>
How much is your time worth? ⏳

A simple sms spam filter 📨

How it works

This app is designed do be as simple as possible. When an sms arrives it shows a notification unless it thinks it is spam. This way, you can continue using your preferred sms app.

This app is
[![Open Source Love svg3](https://badges.frapsoft.com/os/v3/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)
and is available on Google Play: https://play.google.com/store/apps/developer?id=Tools+Lab

Become an early access tester at https://play.google.com/apps/testing/com.toolslab.noisepercolator

###### Technical details and used technologies
This project is written in Kotlin and uses Model-View-Presenter for better testability.  
One of the goals of this app is to work on as many Android versions as pragmatically possible. That's why it can be installed on devices as old as API 16 (Jelly Bean), covering 99.93% of the active Android devices worldwide.

The app is built, unit and instrumentation tested and then deployed to Google Play automatically with the help of Travis and Fastlane Supply.  
