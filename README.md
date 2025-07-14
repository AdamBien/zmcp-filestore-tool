# zmcp-filestore-tool

[ZMCP](https://github.com/AdamBien/zmcp) tool for local file storage operations within a sandboxed directory.

![Agent Duke](agentduke.png)

## Configuration

- `ZMCP_STORAGE_ROOT`: Storage directory (default: `./zmcp-filestore`)

## Operations

### read
Read file content
- `fileName`: Target file name (required)

### write  
Write content to file
- `fileName`: Target file name (required)
- `content`: File content (required)

### list
List all files in storage directory

### delete
Delete a file
- `fileName`: Target file name (required)

## Overview

A file storage tool implementing the Boundary Control Entity (BCE) architectural pattern. Provides read, write, list, and delete operations for files within a configured storage directory.

## Operations

- **read**: Read file content from storage
- **write**: Write content to a file
- **list**: List all files in storage directory
- **delete**: Delete a file from storage

## Usage

The tool accepts the following parameters:
- `operation` (required): Operation to perform (read, write, list, delete)
- `path`: File path relative to storage root (required for read, write, delete)
- `content`: File content for write operations

Files are stored in the `./zmcp-filestore` directory by default.
