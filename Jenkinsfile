pipeline {
    agent any
    environment {
        BACKEND_DIR = "backend"
        FRONTEND_DIR = "frontend"
    }
    triggers {
        githubPush()
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
                         id: 'DeployApproval', // ID giúp theo dõi input
                         message: 'Bạn có chắc chắn muốn deploy nhánh main?',
                         parameters: [
                         choice(name: 'CONFIRM_DEPLOY', choices: ['No', 'Yes'], description: 'Chọn Yes để xác nhận deploy'),
                         text(name: 'REASON', defaultValue: '', description: 'Nhập lý do deploy')
                        ] )

                     if (userInput['CONFIRM_DEPLOY'] != 'Yes') {
                        error "🚨 Deployment bị hủy bởi người dùng với lí do: ${userInput['REASON']}"
                        }

                         echo "✅ Deployment đã được xác nhận với lý do: ${userInput['REASON']}"
                }
            }
        }

        stage('Deploy to Dev Server') {
            when {
                branch 'dev'
            }
            steps {
                script {
                    deployToDevServer()
                }
            }
        }

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
        git reset --hard HEAD   && \
        git clean -fd   && \
        git pull origin dev && \
        chmod +x backend/mvnw && \
        # Build Backend (Dev Mode)
        sudo systemctl stop springboot && \
        cd backend && ./mvnw clean package -DskipTests && \
        sudo systemctl restart springboot && \
        
        # Build Frontend (Dev Mode)
        cd /home/ubuntu/canteenManagement/frontend && \
        npm install && \
        export NODE_OPTIONS='--max-old-space-size=4096' && \
        ng build  && \
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
        git reset --hard HEAD   && \
        git clean -fd   && \
        git pull origin main && \
        chmod +x backend/mvnw && \
        # Build Backend (Prod Mode)
        sudo systemctl stop springboot && \
        cd backend && ./mvnw clean package -DskipTests && \
        sudo systemctl restart springboot && \
        
        # Build Frontend (Prod Mode)
        cd /home/ubuntu/canteenManagement/frontend && \
        npm install && \
        export NODE_OPTIONS='--max-old-space-size=4096' && \
        ng build --configuration=production && \
        sudo cp -r dist/sakai-ng/* /var/www/frontend/ && \
        sudo systemctl restart nginx
    "
"""
    }
}
