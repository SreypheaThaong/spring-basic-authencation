pipeline {
    agent any

    environment {
        REGISTRY = "phea12"                // 🔧 Change to your Docker Hub username
        IMAGE_NAME = "spring-homework-image" // 🔧 Change to your image name
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
                        error "❌ No build.gradle or pom.xml found! Cannot determine build tool."
                    }
                }
            }
        }

        stage('Build JAR') {
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
                    sh """
                        docker build -t ${REGISTRY}/${IMAGE_NAME}:latest .
                    """
                }
            }
        }

        stage('Push Docker Image') {
            when {
                expression { return env.DOCKERHUB_TOKEN && env.DOCKERHUB_USER }
            }
            steps {
                script {
                    echo "📦 Pushing Docker image to Docker Hub..."
                    sh """
                        echo ${DOCKERHUB_TOKEN} | docker login -u ${DOCKERHUB_USER} --password-stdin
                        docker push ${REGISTRY}/${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy (Optional)') {
            when {
                expression { return fileExists('k8s/deployment.yaml') }
            }
            steps {
                echo "🚀 Deploying to Kubernetes via ArgoCD or kubectl..."
                sh """
                    # Example deploy command (customize as needed)
                    kubectl apply -f k8s/
                """
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
