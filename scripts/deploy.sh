#bin/bash
cd /var/play/blog
git fetch
git merge origin/master
/var/play/play-2.1.0/play clean compile stage
chmod +x target/start
sudo service lindseysblog restart


