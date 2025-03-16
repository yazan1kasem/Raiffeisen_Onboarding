echo "Initializing VM"

#!/bin/bash
# Update package lists
sudo apt-get update

# Install prerequisites
sudo apt-get install -y \
  apt-transport-https \
  ca-certificates \
  curl \
  software-properties-common

# Add Docker's official GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

# Add Docker repository
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

# Install Docker
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io

# Start Docker service
sudo systemctl start docker
sudo systemctl enable docker

# Add the VM's system user to docker group
sudo usermod -aG docker azureuser

# add docker compose file into vm

TOKEN=<secret>
echo "$TOKEN" | sudo docker login registry.gitlab.com -u maximiliankraft --password-stdin

cat > docker-compose.yml << EOF
services:
  springimage:
    ports:
      - 80:8080
    image: registry.gitlab.com/maximiliankraft/5xhbgm_server_2425:v0.4
    depends_on:
      - db
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=fhir
    volumes:
      - mysqlvolume:/var/lib/mysql

  db:
    image: mysql
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=fhir
    volumes:
      - mysqlvolume:/var/lib/mysql
volumes:
  mysqlvolume:
EOF

sudo docker compose up db -d
sleep 10
sudo docker compose up springimage -d