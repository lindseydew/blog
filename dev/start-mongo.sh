#!/bin/bash

mongodir=~/mongo/blogs
mongoversion=2.4.4

mkdir -p $mongodir/downloads
mkdir -p $mongodir/db/node1
#mkdir -p $mongodir/db/node2
#mkdir -p $mongodir/db/node3
mkdir -p $mongodir/logs

case `uname` in
	Linux)
		mongoFile=mongodb-linux-x86_64-${mongoversion}.tgz
		mongoPath=mongodb-linux-x86_64-${mongoversion}
		mongoOs="linux"
		;;

	Darwin)
		mongoFile=mongodb-osx-x86_64-${mongoversion}.tgz
		mongoPath=mongodb-osx-x86_64-${mongoversion}
		mongoOs="osx"
		;;

	*)
		echo "Operating system not supported"
		exit 1
esac

echo Stopping mongodb
$mongodir/$mongoPath/bin/mongo admin --port=27000 --eval "db.shutdownServer()"

if [ ! -f "$mongodir/downloads/$mongoFile" ]; then
	echo "Downloading http://fastdl.mongodb.org/$mongoOs/$mongoFile"
	cd $mongodir/downloads
	wget "http://fastdl.mongodb.org/$mongoOs/$mongoFile"
	echo Extracting to $mongodir
	tar -C $mongodir -xzf $mongoFile
fi

cd $mongodir
echo Starting mongodb node-1
$mongodir/$mongoPath/bin/mongod --dbpath $mongodir/db/node1 \
	--logpath $mongodir/logs/mongodb-node1.log \
	-v \
	--smallfiles \
	--rest \
	--pidfilepath $mongodir/mongo.pid \
	--fork \
	--directoryperdb \
	--replSet gonads \
	--port 27000

echo Wait to let the nodes start up
sleep 5

echo Init the replica set
$mongodir/$mongoPath/bin/mongo admin --port=27000 --eval 'db.runCommand({"replSetInitiate" : {"_id" : "gonads",	 "members" : [ { "_id" : 1, "host" : "localhost:27000" }]}})'

echo Done