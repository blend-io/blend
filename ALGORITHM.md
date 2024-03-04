# The Algorithm

Blend follows a 3 step process - *flatten*, *transform* and *deflatten* to perform object mapping.

### Step 1 - Flatten
Removes the hierarchy/nested JSON structure by converting all nodes to leaf nodes. Nested fields are aggregated using a dot (.) separated notation.

### Step 3 - Transform
Iterated over the patches provided in *YAML* file and applies them one by one. The *keys* present in *fromField* are mapped to *toField*

### Step 3 - De-flatten
Build back the nested JSON structure, inverse of *Step 1* - resulting the target structure.

[Algo](https://private-user-images.githubusercontent.com/47689668/309869342-1b7f752e-9470-4f9a-b708-3fe9d85820e2.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MDk1Nzg4NTksIm5iZiI6MTcwOTU3ODU1OSwicGF0aCI6Ii80NzY4OTY2OC8zMDk4NjkzNDItMWI3Zjc1MmUtOTQ3MC00ZjlhLWI3MDgtM2ZlOWQ4NTgyMGUyLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDAzMDQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwMzA0VDE4NTU1OVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWYwOTQ5NjViYjRmY2Y1Yzc4ZjI1MTRiOGUzYzEzMGYxY2VkZThiYjUxNjU0YTI5NWQxNzEzMzJkNjc4YWM0M2MmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.ZB8Kcv-yuDb3hY3ujYOTT1cU76xnP9cH1nQzsCQWkvU)
