# syringe

Clojure code suitable for dev profile injection

## Usage

### Leiningen

####
profiles.clj dependencies
```clojure
[waffletower/syringe "0.1.0"]
```

####
profiles.clj injections
```clojure
(use 'syringe.dose)
(use 'syringe.midje)
```

## License

Copyright © 2017 Christopher Penrose

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
