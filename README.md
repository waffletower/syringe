# syringe

Clojure code suitable for dev profile injection

## Usage

### tools.deps

for deps.edn:
``` clojure
waffletower/syringe {:mvn/version "1.1.0"}
```

See:  https://insideclojure.org/2020/02/11/custom-repl/

### Leiningen

####
profiles.clj dependencies:
```clojure
:dependencies [[waffletower/syringe "1.1.0"]]
```

####
profiles.clj injections
```clojure
:injections [(use 'syringe.dose)]
```

## License

Copyright © 2017-2025 Christopher Penrose

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
