pipeline {
    agent any
    environment {
        BACKEND_DIR = "backend"
        FRONTEND_DIR = "frontend"
        BRANCH = env.BRANCH_NAME // Lấy tên nhánh hiện tại
    }
     triggers {
        // Chỉ chạy tự động nếu là nhánh "dev"
        cron(BRANCH == 'dev' ? '15 21 * * *' : '')
    }
    stages {
        stage('Checkout Code') {
            steps {
                script {
                    checkout scm
                }
            }
        }
        stage('Approve Deployment (main)') {
            when {
                branch 'main'
            }
            steps {
                script {
                    

                    def userInput = input(
                        message: 'Có muốn deploy nhánh main không?',
                        parameters: [
                            booleanParam(name: 'Deploy', defaultValue: false, description: 'Chọn để xác nhận deploy')
                        ]
                    )

                    if (!userInput) {
                        error "Deployment bị hủy!"
                    }
                }
            }
        }

        // stage('Deploy to Dev Server') {
        //     when {
        //         branch 'dev'
        //     }
        //     steps {
        //         script {
        //             deployToDevServer()
        //         }
        //     }
        // }

        stage('Deploy to Prod Server') {
            when {
                branch 'main'
            }
            steps {
                script {
                    deployToProdServer()
                }
            }
        }
        // stage('Deploy to Server') {
        //     steps {
        //         script {
        //             if (env.BRANCH_NAME == 'dev') {
        //                 deployToDevServer()
        //             } else if (env.BRANCH_NAME == 'main') {
        //                 deployToProdServer()
        //             } else {
        //                 error "Unknown branch, cannot determine deployment server!"
        //             }
        //         }
        //     }
        // }
    }
}

def deployToDevServer() {
    def SERVER_IP = 'ubuntu@13.55.218.9' // Dev Server IP
    sshagent(['jenkins-ssh-key']) {
       sh """
    ssh -o StrictHostKeyChecking=no ${SERVER_IP} "
        cd /home/ubuntu/canteenManagement && \
        git pull origin dev && \
        
        # Build Backend (Dev Mode)
        cd backend && ./mvnw clean package -DskipTests && \
        sudo systemctl restart springboot && \
        
        # Build Frontend (Dev Mode)
        cd /home/ubuntu/canteenManagement/frontend && \
        npm install && \
        ng build  && \
        export NODE_OPTIONS='--max-old-space-size=4096' && \
        sudo cp -r dist/sakai-ng/* /var/www/frontend/ && \
        sudo systemctl restart nginx
    "
"""

    }
}

def deployToProdServer() {
    def SERVER_IP = 'ubuntu@3.107.198.234' // Prod Server IP
    sshagent(['jenkins-ssh-key']) {
       sh """
    ssh -o StrictHostKeyChecking=no ${SERVER_IP} "
        cd /home/ubuntu/canteenManagement && \
        git pull origin main && \
        
        # Build Backend (Prod Mode)
        cd backend && ./mvnw clean package -DskipTests && \
        sudo systemctl restart springboot && \
        
        # Build Frontend (Prod Mode)
        cd /home/ubuntu/canteenManagement/frontend && \
        npm install && \
        ng build --configuration=production && \
        export NODE_OPTIONS='--max-old-space-size=4096' && \
        sudo cp -r dist/sakai-ng/* /var/www/frontend/ && \
        sudo systemctl restart nginx
    "
"""

    }
}
