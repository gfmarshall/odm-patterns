# IBM ODM Development Environment

This repository contains configuration and guidelines for IBM Operational Decision Manager (ODM) development.

## Getting Started with ODM Docker Environment

### Prerequisites

- Docker and Docker Compose installed on your system
- At least 4GB of RAM available for Docker
- 5GB of free disk space

### Starting the ODM Environment

1. Create the required data directories:
```bash
mkdir -p odm-data/decisioncenter odm-data/decisionserver/decisionrunner odm-data/decisionserver/decisionserverconsole odm-data/teamserver
```

2. Start the ODM container:
```bash
docker compose up -d
```

3. Wait for ODM to initialize (this may take 5-10 minutes on first startup)

4. Access ODM components through the following URLs:
   - Decision Center (business user interface): http://localhost:9060/decisioncenter/
   - Decision Server Console (deployment & testing): http://localhost:9060/res/
   - Decision Runner (execution & testing): http://localhost:9060/DecisionRunner/

### Default Credentials

- Username: `odmAdmin`
- Password: `odmAdmin`

### Persisted Data

All your work is automatically persisted in the mapped volumes under the `odm-data` directory. This ensures that your rules, projects, and configurations are preserved when you restart the container.

| Directory | Purpose |
|-----------|----------|
| `odm-data/decisioncenter` | Stores Decision Center artifacts including projects, rules, and decision services |
| `odm-data/decisionserver/decisionrunner` | Stores Decision Runner configurations and test data |
| `odm-data/decisionserver/decisionserverconsole` | Stores Decision Server Console configurations and deployment information |
| `odm-data/teamserver` | Stores TeamServer configurations for collaborative features |

### Using ODM Components

#### Decision Center
The primary interface for business users to create and manage rule projects. This is where you'll:
- Create and manage rule projects
- Define business rules and decision tables
- Create and validate rule flows
- Manage deployments

#### Decision Server Console
The administrative interface for Decision Server where you'll:
- Deploy rule projects as RuleApps
- Test rule execution
- Monitor rule service performance

#### Decision Runner
A testing tool for validating rule execution where you'll:
- Create and run test scenarios
- Validate rule behavior with test data
- Compare test results across different rule versions

### Stopping the Environment

```bash
docker compose down
```

### Customizing ODM Configuration

For advanced configuration needs, you can edit the Docker Compose file to adjust:

- Memory and CPU allocation
- Database configuration
- Security settings
- Additional environment variables

## Project Guidelines

This repository contains the following guidelines:

- [ODM Workspace Rules](./odm-workspace-rules.md) - General rules for ODM development
- [Example Project Rules](./example-project-rules.md) - Sample project-specific rules for reference

## Working with Rule Projects

Follow the established guidelines in the workspace rules documents when creating and managing rule projects. This ensures consistency and maintainability across projects.
