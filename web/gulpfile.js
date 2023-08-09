var path = require('path')
var gulp = require('gulp')
var cleanCSS = require('gulp-clean-css');
var cssWrap = require('gulp-css-wrap');

var customThemeName='.orange-theme';
// 主题1
// gulp.task('css-wrap', function() {
//   return gulp.src(path.resolve('./theme/theme_orange/index.css'))
//     .pipe(cssWrap({selector:customThemeName}))
//     .pipe(cleanCSS())   
//     .pipe(gulp.dest('src/assets/theme/theme_orange'));
// });

gulp.task('css-wrap', function() {
  return gulp.src(path.resolve('./theme/theme_orange/layout.css'))
    .pipe(cssWrap({selector:customThemeName}))
    .pipe(cleanCSS())   
    .pipe(gulp.dest('src/assets/theme/theme_orange'));
});

// gulp.task('move-font', function() {
//   return gulp.src(['./theme/theme1/fonts/**']).pipe(gulp.dest('src/assets/theme/theme1/fonts'));
// });
// 主题2
// var customThemeName2='.light-theme';
// gulp.task('css-wrap', function() {
//   return gulp.src(path.resolve('./theme/theme2/index.css'))
//     .pipe(cssWrap({selector:customThemeName2}))
//     .pipe(cleanCSS())   
//     .pipe(gulp.dest('src/assets/theme/theme2'));
// });

// gulp.task('css-wrap', function() {
//   return gulp.src(path.resolve('./theme/theme2/layout.css'))
//     .pipe(cssWrap({selector:customThemeName}))
//     .pipe(cleanCSS())   
//     .pipe(gulp.dest('src/assets/theme/theme2'));
// });

// gulp.task('move-font', function() {
//   return gulp.src(['./theme/theme2/fonts/**']).pipe(gulp.dest('src/assets/theme/theme2/fonts'));
// });
// gulp.task('default',['css-wrap','move-font']);

// var customThemeName='.blue-theme';
// 主题3
// gulp.task('css-wrap', function() {
//   return gulp.src(path.resolve('./theme/theme_blue/index.css'))
//     .pipe(cssWrap({selector:customThemeName}))
//     .pipe(cleanCSS())   
//     .pipe(gulp.dest('src/assets/theme/theme_blue'));
// });

// gulp.task('css-wrap', function() {
//   return gulp.src(path.resolve('./theme/theme_blue/layout.css'))
//     .pipe(cssWrap({selector:customThemeName}))
//     .pipe(cleanCSS())   
//     .pipe(gulp.dest('src/assets/theme/theme_blue'));
// });

// gulp.task('move-font', function() {
//   return gulp.src(['./theme/theme_blue/fonts/**']).pipe(gulp.dest('src/assets/theme/theme_blue/fonts'));
// });