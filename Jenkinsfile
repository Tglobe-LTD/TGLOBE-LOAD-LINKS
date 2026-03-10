pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = "tglobe-app:latest"
        SONAR_URL = "http://localhost:9000" // Change to your SonarQube IP
    }

    stages {
        stage('Compile & Test') {
            steps { 
                sh 'mvn clean package -DskipTests' 
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // This uses the Maven plugin for SonarQube
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Build Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Vulnerability Scan (Trivy)') {
            steps {
                echo "Running security scan on Tglobe Image..."
                // Scans the image and stops the pipeline if HIGH or CRITICAL issues are found
                sh "trivy image --severity HIGH,CRITICAL --exit-code 1 ${DOCKER_IMAGE}"
            }
        }

        stage('Deploy to Minikube') {
            steps {
                echo "Deploying Tglobe Load-Links to Port Harcourt Cluster..."
                sh 'kubectl apply -f k8s/deployment.yaml'
                sh 'kubectl apply -f k8s/service.yaml'
            }
        }
    }
    
    post {
        always {
            echo "Cleaning up workspace..."
            cleanWs()
        }
    }
}
