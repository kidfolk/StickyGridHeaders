# StickyGridHeaders

StickyGridHeaders is an Android library that provides a `GridView` that shows
items in sections with headers. By default the section headers stick to the top
like the People app in Android 4.x but this can be turned off.
StickyGridHeaders also automatically sizes its rows to the largest item in the
row.

StickyGridHeaders has been designed to be adapter compatible with, and was
inspired by, [StickyListHeaders](http://github.com/emilsjolander/StickyListHeaders).


## Usage

`StickyGridHeadersGridView` replaces the use of a `GridView` in your
application and is used in the same manner. The `ListAdapter` must implement
one of either `StickyGridHeadersBaseAdapter` or
`StickyGridHeadersSimpleAdapter`. The choice of the implementation allows you
to decide between the 'Simple' version which automatically does some housework
to enable the grid sections and headers, or the 'Base' version which leaves
that work to you so you can implement a specific solution for your data set.

The simple adapter interface has the same signature as
`StickyListHeadersAdapter` so if you are using the StickyListHeaders library
your adapters are already ready for StickyGridHeaders.

Included in this project is an example demonstrating the use of the library.


## License
```
Copyright 2013 Tonic Artos

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```