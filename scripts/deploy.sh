#bin/bash
cd /var/play
if [ -d blog.orig ]; then
    rm -rf blog.orig
fi
mv blog blog.orig
git clone ~/git/blog.git
cd blog
/var/play/play-2.1.0/play clean compile stage
chmod +x target/start
sudo service lindseysblog restart
rm -rf blog.orig


