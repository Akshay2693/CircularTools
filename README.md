CircularTools
==============
Circle based animations for Android (min. API 11)

Currently implemented:
- Circular reveal
- Circular transform
- <a href="http://material-design.storage.googleapis.com/videos/animation-responsive-interation-radialReact-example_large_xhdpi.webm">Radial reaction</a>

|**Reveal:**<a href="https://youtu.be/g83nwbi33c0">YouTube</a>|**Transform:**<a href="https://youtu.be/96eBHwWxTiA">YouTube</a>|**Radial reaction:**<a href="https://youtu.be/jv6fs12UJHo">YouTube</a>|
|---------------|-----------|-----------|
|<img src="http://i.imgur.com/7RADHID.gif" alt="Reveal DEMO" width="240" height="400" border="10" />|<img src="http://i.imgur.com/k5kEs9h.gif" alt="Transform DEMO" width="240" height="400" border="10" />|<img src="http://i.imgur.com/XkPNT4V.gif" alt="Radial reaction DEMO" width="240" height="400" border="10" />|


Sample
------
<a href="https://github.com/Gordi90/CircularTools/releases">Demo in the source and releases</a>

Note
-----
- it's a fork from https://github.com/ozodrukh/CircularReveal/
- independent from Jake Wharton's NineOldsAndroid
- the returned `animator` is an `ObjectAnimator` so you can reverse it.
 
Limitations
-----------
**For reveal and transform:**
- it will never use the native `ViewAnimationUtils.createCircularReveal` method
- currently there is an issue: views with elevation cannot be animated correctly on Lollipop and above.
	- workaround A: set the LayerType of the target (or source) view to LayerType.SOFTWARE
	- workaround B: wrap your target or (source) view with a simple layout, without elevation, and animate that. Demo reveal uses this method.
- hardware acceleration cannot be used in every situation. See table below:

|               | API 11-17 |  API 18+  |
|---------------|-----------|-----------|
|   **Reveal**  |  Software |  Hardware |
| **Transform** |  Software |  Hardware |

**For radial reaction**
- only one reaction can be animated at the same time, in the same `RadialReactionParent`
- only one layout implemented yet: a LinearLayout.
- once an affectedView is reached, it will be removed from the list in the `RadialReactionParent`. If you want to animate multiple times, you have to add the views again. See demo for details.

Usage
------

For reveal and transform you have to wrap your animated views with a `CircularFrameLayout`.

```xml
<hu.aut.utillib.circular.widget.CircularFrameLayout
        android:id="@+id/simple_reveal"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    
    <!-- Put any child views here if you want, it's stock FrameLayout  -->

</hu.aut.utillib.circular.widget.CircularFrameLayout>
```
**Transform:**
```java
//myTargetView & mySourceView are children in the CircularFrameLayout
float finalRadius = CircularAnimationUtils.hypo(width, height);

//getCenter computes from 2 view: One is touched, and one will be animated, but you can use anything for center
int[] center = CircularAnimationUtils.getCenter(fab, myTargetView);

animator = CircularAnimationUtils.createCircularTransform(myTargetView, mySourceView, center[0], center[1], 0F, finalRadius);
animator.setInterpolator(new AccelerateDecelerateInterpolator());
animator.setDuration(1500);
animator.start();

```

**Reveal:**
```java
//myView is a child in the CircularFrameLayout
float finalRadius = CircularAnimationUtils.hypo(width, height);

//getCenter computes from 2 view: One is touched, and one will be animated, but you can use anything for center
int[] center = CircularAnimationUtils.getCenter(fab, myView);

animator = CircularAnimationUtils.createCircularReveal(myView, center[0], center[1], 0, finalRadius);
animator.setInterpolator(new AccelerateDecelerateInterpolator());
animator.setDuration(1500);
animator.start();      

```

For radial reaction you have to wrap your views -that are affected by the radial reaction- with a `RadialReactionParent`. 
- Wrap your view
- Add a listener to it (this listener will be notified when the invisible circle reaches an affected view)
- Add views to the parent's watchlist by calling `addAffectedView(View v)`
- Create animation and start it
- Get notified when the radial reaction's invisible circle reaches one of the affected view

```xml
<hu.aut.utillib.circular.widget.RadialReactionLinearLayout 
    android:id="@+id/reaction_parent"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

	<FrameLayout
		android:id="@+id/child_1"
		android:layout_width="80dp"
		android:layout_height="80dp"
		android:layout_alignParentTop="true"
		android:layout_margin="10dp"
		android:background="@color/material_deep_teal_500"
		android:visibility="invisible" />

    <!-- Put any child views here if you want, it's stock LinearLayout -->

</hu.aut.utillib.circular.widget.RadialReactionLinearLayout>
```
**Radial reaction:**
```java
reactionParent = (RadialReactionParent) view.findViewById(R.id.reaction_parent);
reactionParent.addListener(this);

//add views
reactionParent.addAffectedView(child1);

ObjectAnimator animator = CircularAnimationUtils.createRadialReaction(reactionParent, fab, "action");
animator.setInterpolator(new AccelerateDecelerateInterpolator());
animator.setDuration(1500);
animator.start();

...

    @Override
    public void onRadialReaction(View affectedView, String action) {
	//Do what you want with the view    
    }

```

How to add dependency
=====================

This library is not released in Maven Central, but instead you can use [JitPack](https://www.jitpack.io/)

add remote maven url

```groovy
	repositories {
	    maven {
	        url "https://jitpack.io"
	    }
	}
```

then add a library dependency

```groovy
	dependencies {
	        compile 'com.github.AutSoft:CircularTools:1.1.0'
	}
```


License
--------

    The MIT License (MIT)

    Copyright (c) 2015 AutSoft Kft.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
