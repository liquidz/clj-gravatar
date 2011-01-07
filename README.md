# clj-gravatar

gravatar api wrapper for clojure
http://ja.gravatar.com/

## Usage

### gravatar image

    (gravatar-image "your@mail.address")

#### option
 * size
  * avatar image size
 * default
  * default image url
 * secure?
  * https flag. default is false

#### option example

    (gravatar-image "your@mail.address" :size 24 :default "http://hoge.com/fuga.png" :secure? true)

### gravatar profile

    (gravatar-profile "your@mail.address")

#### option
 * secure?
  * https flag. default is false

#### option example

    (gravatar-profile "your@mail.address" :secure? true)

## License

Copyright (C) 2011 Masashi Iizuka

Distributed under the Eclipse Public License, the same as Clojure.
