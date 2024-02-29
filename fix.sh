#!/bin/sh
username=`git config user.name`
if [ -z $username ];then
	username=`whoami`
fi
comment=$1
if [ -z "$comment" ];then
	comment="$username commit at `date +'%Y-%m-%d %H:%M:%S'`"
fi
git add .;git commit -m "$comment" ;git push

if [ -f ~/fix.sh ];then
	cp ~/fix.sh ./fix.sh
else 
	cp ./fix.sh ~/fix.sh
fi
