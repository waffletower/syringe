# syringe

Clojure code suitable for dev profile injection

## Usage

### Leiningen

####
profiles.clj dependencies
```clojure
:dependencies [[waffletower/syringe "0.4.0"]]
```

####
profiles.clj injections
```clojure
:injections [(use 'syringe.dose)]
```

## License

Copyright © 2017+ Christopher Penrose

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
