pipeline {
    agent any

    environment {
        REGISTRY = "phea12"
        IMAGE_NAME = "spring-homework-image"
        APP_PORT = "8080"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "📥 Checking out source code..."
                checkout scm
            }
        }

        stage('Detect Build Tool') {
            steps {
                script {
                    if (fileExists('pom.xml')) {
                        env.BUILD_TOOL = "maven"
                        echo "🧩 Detected Maven project."
                    } else if (fileExists('build.gradle') || fileExists('build.gradle.kts')) {
                        env.BUILD_TOOL = "gradle"
                        echo "🧩 Detected Gradle project."
                    } else {
                        error "❌ No build.gradle or pom.xml found!"
                    }
                }
            }
        }

        stage('Build JAR') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    // Fixed: No volume mount to avoid permission issues
                    reuseNode true
                }
            }
            steps {
                script {
                    if (env.BUILD_TOOL == "maven") {
                        sh 'mvn -B -DskipTests clean package'
                    } else if (env.BUILD_TOOL == "gradle") {
                        sh './gradlew build -x test'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "🐳 Building Docker image..."
                    sh "docker build -t ${REGISTRY}/${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo "📦 Pushing Docker image to Docker Hub..."
                    withCredentials([usernamePassword(credentialsId: 'Docker-token', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_TOKEN')]) {
                        sh '''
                            echo $DOCKERHUB_TOKEN | docker login -u $DOCKERHUB_USER --password-stdin
                            docker push ${REGISTRY}/${IMAGE_NAME}:latest
                        '''
                    }
                }
            }
        }

        stage('Deploy (Optional)') {
            when {
                expression { return fileExists('k8s/deployment.yaml') }
            }
            steps {
                echo "🚀 Deploying to Kubernetes..."
                sh "kubectl apply -f k8s/"
            }
        }
    }

    post {
        success {
            echo "✅ Build and Docker image creation successful!"
        }
        failure {
            echo "❌ Build failed! Check logs for details."
        }
    }
}