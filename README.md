# lacinia-backends-qliksense

SPARQL backend for qliksense Repository api

## Installation


## Usage

Create a configuration file and set teh environment

	export LACINIA_BACKEND_QLIKSENSE_CONFIG=/home/matteo/src/github/lacinia-backend-qliksense-config.edn

Add this library to your project: see https://clojars.org/matteoredaelli/lacinia-backend-qliksense


## Sources

Run the project directly:

	$ clojure -m matteoredaelli.lacinia-backend-qliksense

Run the project's tests (they'll fail until you edit them):

	$ clojure -A:test:runner -M:runner

Build an uberjar:

	$ clojure -A:uberjar -M:uberjar

Run that uberjar:

	$ java -jar lacinia-backend-qliksense.jar

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2020 Matteo

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
