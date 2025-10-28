pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'phea12/spring-homework-image'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-token')
        GITHUB_CREDENTIALS = credentials('github-token')

        // Auto-detect build tool (Maven or Gradle)
        BUILD_TOOL = fileExists('pom.xml') ? 'maven' : 'gradle'
    }

    tools {
        maven 'Maven3'
        gradle 'Gradle7'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-token',
                    url: "${env.GIT_URL}",
                    branch: "${env.BRANCH_NAME ?: 'main'}"
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    if (env.BUILD_TOOL == 'maven') {
                        sh 'mvn clean package'
                    } else {
                        sh 'gradle clean build'
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { return fileExists('sonar-project.properties') }
            }
            steps {
                script {
                    if (env.BUILD_TOOL == 'maven') {
                        sh 'mvn sonar:sonar'
                    } else {
                        sh 'gradle sonarqube'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                        docker build \
                            --build-arg BUILD_TOOL=${env.BUILD_TOOL} \
                            -t ${DOCKER_IMAGE}:${DOCKER_TAG} \
                            -t ${DOCKER_IMAGE}:latest \
                            .
                    """
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Clean Up') {
            steps {
                sh "docker rmi ${DOCKER_IMAGE}:${DOCKER_TAG} || true"
                sh "docker rmi ${DOCKER_IMAGE}:latest || true"
                sh 'docker logout'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Example deployment - adjust based on your infrastructure
                    echo "Deploying ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    // Add your deployment commands here
                    // e.g., kubectl, docker-compose, AWS ECS, etc.
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}