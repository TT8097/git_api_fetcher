# gruszka_rekrutacja_-_
git repo fetcher
# GitHub Repository Explorer

A simple Spring Boot application that fetches GitHub repositories and branches for a given user using the GitHub REST API.

## 🚀 Features

- Get public repositories of a GitHub user
- Fetch branches for each repository
- Uses Java HTTP client and Jackson for JSON processing

## 🛠️ Technologies

- Java 21
- Spring Boot
- JUnit + Mockito
- Jackson
- GitHub REST API
## 📡 API Endpoint

The application exposes a single REST endpoint to retrieve repositories and their branches for a given GitHub user.

### `GET test/get/{name}`

- **Description**: Returns a list of public repositories for the GitHub user specified by `{name}`, including their branches.
- **Path Parameter**:
  - `name` (String): GitHub username (nickname) of the client.

#### ✅ Example Request:
http://localhost:8080/test/get/TT8097
## ⚠️ Limitations

- ❗ **Authorization is intentionally not implemented**, as per project requirements.
- 🔄 Because the GitHub API is used without authentication, the application is limited to **60 requests per hour per IP address** (as per [GitHub API rate limiting](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting)).
- To increase this limit in the future, personal access tokens could be used with Authorization headers.
