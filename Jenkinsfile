pipeline {

    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    environment {
        BACKEND_DIR = 'apps/backend'
        FRONTEND_DIR = 'apps/frontend'
        QA_AUTOMATION_DIR = 'apps/qa-automation'
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

        stage('API Tests (Playwright)') {
            steps {
                sh 'docker compose up -d --build postgres backend'
                sh '''
                    for i in $(seq 1 30); do
                        curl -sf http://localhost:8080/api/products > /dev/null && break
                        sleep 2
                    done
                '''
                dir(QA_AUTOMATION_DIR) {
                    // Jenkins corre como contenedor sibling del stack (Docker-outside-of-Docker):
                    // no está en la red que crea `docker compose`, así que usa el gateway del
                    // host de Docker Desktop para llegar al puerto publicado del backend.
                    // En Linux nativo (sin Docker Desktop) reemplazar por --add-host=host.docker.internal:host-gateway
                    // en el contenedor de Jenkins, o usar la IP del host directamente.
                    sh 'pnpm install --frozen-lockfile'
                    sh 'BASE_URL=http://host.docker.internal:8080/api npx playwright test'
                }
            }
            post {
                always {
                    sh 'docker compose down -v'
                    allure includeProperties: false, results: [[path: "${QA_AUTOMATION_DIR}/allure-results"]]
                }
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
