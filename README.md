# user-service

# Kaniko

CD into K8s, start a kubernetes cluster (e.g. minikube) then create a secret<br/>
kubectl create secret docker-registry regcred --docker-server=https://index.docker.io/v1/ --docker-username=<your-name> --docker-password=<your-pword><br/>
then run<br/>
kubectl apply -f kaniko.yml<br/>

# Helm

