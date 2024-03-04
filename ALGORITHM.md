# The Algorithm

Blend follows a 3 step process - *flatten*, *transform* and *deflatten* to perform object mapping.

### Step 1 - Flatten
Removes the hierarchy/nested JSON structure by converting all nodes to leaf nodes. Nested fields are aggregated using a dot (.) separated notation.

### Step 3 - Transform
Iterated over the patches provided in *YAML* file and applies them one by one. The *keys* present in *fromField* are mapped to *toField*

### Step 3 - De-flatten
Build back the nested JSON structure, inverse of *Step 1* - resulting the target structure.

![Blend - The Algo](https://ibb.co/qB3nMBL)
