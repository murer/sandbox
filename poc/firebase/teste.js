import React, { Component } from 'react';

import firebase from 'firebase/app';
import 'firebase/auth';

var config = {
    apiKey: "AIzaSyC3sWq8Efvd0k6ETKBUfY1BgARIpf5igtc",
    authDomain: "dextra-parking.firebaseapp.com",
    databaseURL: "https://dextra-parking.firebaseio.com",
    projectId: "dextra-parking",
    storageBucket: "dextra-parking.appspot.com",
    messagingSenderId: "164824251074"
};

var provider = new firebase.auth.GoogleAuthProvider();
provider.addScope('https://www.googleapis.com/auth/calendar.readonly');
provider.setCustomParameters({
    prompt: 'select_account',
    hd: "dextra-sw.com"
});

firebase.initializeApp(config);
window.fb = firebase

