# sabisu
A Clojure micro-service options configuration library

## Artifact

via Clojars:
`[waffletower/sabisu "0.1.0"]`

## Usage

### sabisu.conf namespace

The conf namespace contains a macro, `(system-options)` and several helper functions, which are intended to simplify  options management for services.  service options are specified with keys, a validation predicate function, and a default values.  Consider this example:

    `(system-options
     bargain-service
     [[:database-connection-string string? "only-the-best-in-security"]
      [:database-host-name string? "demure.bargains.com"]
      [:database-port int? 1998]])`

The `(system-options)` macro creates validation specs for all options and an accessor function (in this case named `(bargain-service-options)`) which can be used at run-time to merge environment variables with the declared default options, and validate the result.

## Tests

`lein midje`

## License

Copyright © 2018 Christopher Penrose

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
