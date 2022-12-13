cd webapp && ./mvnw clean package
cd ..
docker compose build
docker compose push
az webapp config container set --resource-group mktrends --name mktrends --multicontainer-config-type compose --multicontainer-config-file docker-compose.yml
curl -X POST https://$mktrends:sF1oYaPnGW4ZTtW0ndwEqzSwhXyR3fWmDcoLhD5gnben6RHCEw9pgRdCGxGP@mktrends.scm.azurewebsites.net/api/registry/webhook
