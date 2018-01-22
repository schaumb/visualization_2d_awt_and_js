package bxlx.jsweet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Generator {
    public static void main(String[] args) throws IOException {
        List<JsFile> jsFiles = Files.walk(Paths.get("."))
                .map(p -> StreamSupport.stream(p.spliterator(), false).map(Object::toString).collect(Collectors.toCollection(LinkedList::new)))
                .filter(p -> p.size() > 0)
                .peek(p -> {
                    String last = p.getLast();
                    int i = last.indexOf(".");
                    if (i >= 0) {
                        String ext = last.substring(i + 1);
                        p.set(p.size() - 1, last.substring(0, i));
                        p.add(ext);
                    }
                })
                .filter(p -> p.getLast().equals("js"))
                .map(p -> {
                    int index = p.indexOf("bxlx");
                    return p.subList(index == -1 ? p.size() - 1 : index, p.size() - 1);
                })
                .filter(p -> p.size() > 0)
                .map(wholePath -> {
                    try {
                        return new JsFile(wholePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        HashMap<JsFile, HashSet<JsFile>> dependencies = new HashMap<>();
        jsFiles.forEach(j -> dependencies.put(j, new HashSet<>()));

        for (JsFile from : jsFiles) {
            String javaFile = from.getJavaFile();
            for (JsFile to : jsFiles) {
                if (from.equals(to))
                    continue;

                String searchName = to.getClassName();

                boolean bad = true;
                int where = -1;
                while (bad) {
                    ++where;
                    where = javaFile.indexOf(searchName, where);
                    if (where == -1)
                        break;

                    switch (javaFile.charAt(where - 1)) {
                        case '.':
                        case ' ':
                        case '(':
                        case '<':
                            switch (javaFile.charAt(where + searchName.length())) {
                                case '.':
                                case ' ':
                                case '>':
                                case '(':
                                case '<':
                                case '{':
                                case ';':
                                    bad = false;
                            }
                    }
                }

                if (!bad) {
                    dependencies.get(from)
                            .add(to);
                }
            }
        }

        List<JsFile> ordered = new ArrayList<>();

        //while (ordered.size() < jsFiles.size())
        {
            Iterator<Map.Entry<JsFile, HashSet<JsFile>>> iterator = dependencies.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<JsFile, HashSet<JsFile>> next = iterator.next();
                if (next.getValue().size() == 0) {
                    ordered.add(next.getKey());
                    System.err.println(next.getKey().getClassName());
                    iterator.remove();
                }
            }
        }
    }

    public static class JsFile {
        private final List<String> wholePath;
        private String javaFile;
        private String jsFile;

        public JsFile(List<String> wholePath) throws IOException {
            this.wholePath = wholePath;
            javaFile = Files.readAllLines(Paths.get(getJavaName())).stream().collect(Collectors.joining("\n"));
            jsFile = Files.readAllLines(Paths.get(getJsName())).stream().collect(Collectors.joining("\n"));
        }

        public String getJavaFile() {
            return javaFile;
        }

        public String getJsFile() {
            return jsFile;
        }

        public String getClassName() {
            return wholePath.get(wholePath.size() - 1);
        }

        String getJavaName() {
            return wholePath.stream().collect(Collectors.joining("/", "src/main/java/", ".java"));
        }

        String getJsName() {
            return wholePath.stream().collect(Collectors.joining("/", "target/ts/", ".js"));
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof JsFile)) return false;
            JsFile jsFile = (JsFile) o;
            return Objects.equals(wholePath, jsFile.wholePath);
        }

        @Override
        public int hashCode() {
            return Objects.hash(wholePath);
        }
    }
}
