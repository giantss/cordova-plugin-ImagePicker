#!/usr/bin/env node

var fs    = require('fs');
var async = require('async');
var exec  = require('child_process').exec;
var path  = require('path');

var fileExists = function(filePath){
    try {
        return fs.statSync(filePath).isFile();
    }
    catch (err) {
        return false;
    }
};

var root = process.cwd();
var androidManifest;
var cordovaAndroid6Path = path.join(root, 'platforms/android/AndroidManifest.xml');
var cordovaAndroid7Path = path.join(root, 'platforms/android/app/src/main/AndroidManifest.xml');
if(fileExists(cordovaAndroid7Path)){
    androidManifest = cordovaAndroid7Path;
} else if(fileExists(cordovaAndroid6Path)){
    androidManifest = cordovaAndroid6Path;
} else{
    throw "Can't find AndroidManifest.xml";
}

fs.readFile(androidManifest, 'utf8', function(err, data) {
    if(err) throw err;

    var lines = data.split('\n');
    var searchingFor = '<application android:hardwareAccelerated="true"';
    var newManifest = [];
    var largeHeap = 'android:largeHeap="true"';
    lines.forEach(function(line) {
        if(line.trim().indexOf(searchingFor) != -1 && line.trim().indexOf(largeHeap) == -1) {
            newManifest.push(line.replace(/\>$/, ' ') + largeHeap + ">");
        } else {
            newManifest.push(line);
        }
    });

    fs.writeFileSync(androidManifest, newManifest.join('\n'));
});
