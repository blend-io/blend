package io.blend.models;

import java.util.List;

public class BlendConfig {
    private List<Patch> patches;

    public List<Patch> getPatches() {
        return List.copyOf(patches);
    }

    public void setPatches(List<Patch> patches) {
        this.patches = List.copyOf(patches);
    }

    public static class Patch {

        private String fromField;
        private String toField;
        private boolean postDeflatten;
        private TransformConfig transformConfig;

        public boolean isRegularPatch() {
            return !this.postDeflatten;
        }
        public String getFromField() {
            return fromField;
        }

        public String getToField() {
            return toField;
        }

        public boolean isPostDeflatten() {
            return postDeflatten;
        }

        public TransformConfig getTransform() {
            return transformConfig;
        }

        public void setFromField(String fromField) {
            this.fromField = fromField;
        }

        public void setToField(String toField) {
            this.toField = toField;
        }

        public void setPostDeflatten(boolean postDeflatten) {
            this.postDeflatten = postDeflatten;
        }

        public void setTransform(TransformConfig transformConfig) {
            this.transformConfig = transformConfig;
        }

        public static class TransformConfig {

            private String name;
            private String[] args;

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return this.name;
            }

            public void setArgs(String argsString) {
                this.args = argsString.split(",");
            }

            public String[] getArgs() {
                return this.args;
            }
        }
        
    }
}

