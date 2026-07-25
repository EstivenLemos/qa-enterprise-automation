pipeline {

    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    environment {
        BACKEND_DIR = 'apps/backend'
        FRONTEND_DIR = 'apps/frontend'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend: Build & Test') {
            steps {
                dir(BACKEND_DIR) {
                    sh './mvnw -B verify'
                }
            }
            post {
                always {
                    junit testResults: "${BACKEND_DIR}/target/surefire-reports/*.xml,${BACKEND_DIR}/target/failsafe-reports/*.xml", allowEmptyResults: true
                    publishHTML(target: [
                        reportDir: "${BACKEND_DIR}/target/site/jacoco",
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage Report',
                        keepAll: true,
                        alwaysLinkToLastBuild: true
                    ])
                }
            }
        }

        stage('Frontend: Install, Check & Build') {
            steps {
                dir(FRONTEND_DIR) {
                    sh 'corepack enable'
                    sh 'pnpm install --frozen-lockfile'
                    sh 'pnpm run check'
                    sh 'pnpm run build'
                }
            }
        }

        stage('Docker: Build Images') {
            steps {
                sh "docker build -t smartstore-backend:${BUILD_NUMBER} ${BACKEND_DIR}"
                sh "docker build -t smartstore-frontend:${BUILD_NUMBER} ${FRONTEND_DIR}"
            }
        }

    }

    post {
        success {
            echo 'Pipeline completado correctamente.'
        }
        failure {
            echo 'El pipeline falló. Revisa los reportes de JUnit y JaCoCo publicados en este build.'
        }
    }

}
