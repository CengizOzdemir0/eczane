pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'docker.io'
        IMAGE_NAME = 'eczane-app'
        CONTAINER_NAME = 'eczane-app'
        SERVER_HOST = '185.136.206.32'
        SERVER_USER = 'root'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CengizOzdemir0/eczane.git'
            }
        }
        
        stage('Build') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    sh """
                        docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                        docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${IMAGE_NAME}:latest
                    """
                }
            }
        }
        
        stage('Deploy to Server') {
            steps {
                script {
                    sshagent(['server-ssh-credentials']) {
                        sh """
                            # Copy docker-compose and .env files
                            scp docker-compose.yml ${SERVER_USER}@${SERVER_HOST}:/opt/eczane/
                            scp .env ${SERVER_USER}@${SERVER_HOST}:/opt/eczane/
                            
                            # Save and transfer Docker image
                            docker save ${IMAGE_NAME}:latest | gzip > ${IMAGE_NAME}.tar.gz
                            scp ${IMAGE_NAME}.tar.gz ${SERVER_USER}@${SERVER_HOST}:/tmp/
                            
                            # Deploy on server
                            ssh ${SERVER_USER}@${SERVER_HOST} << 'EOF'
                                cd /opt/eczane
                                
                                # Load Docker image
                                docker load < /tmp/${IMAGE_NAME}.tar.gz
                                rm /tmp/${IMAGE_NAME}.tar.gz
                                
                                # Stop and remove old containers
                                docker-compose down
                                
                                # Start new containers
                                docker-compose up -d
                                
                                # Clean up old images
                                docker image prune -f
EOF
                        """
                    }
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    sleep(time: 30, unit: 'SECONDS')
                    sh """
                        ssh ${SERVER_USER}@${SERVER_HOST} 'curl -f http://localhost:8080/api/actuator/health || exit 1'
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
        always {
            cleanWs()
        }
    }
}
