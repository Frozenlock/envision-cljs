var envision = {};

// Component API
envision.Component = function() {};
envision.Component.render = function () {};
envision.Component.draw = function () {};
envision.Component.trigger = function () {};
envision.Component.attach = function () {};
envision.Component.detach = function () {};
envision.Component.destroy = function () {};

envision.components.QuadraticDrawing = function() {};



// Visualization API

envision.Visualization = function () {};
envision.Visualization.add = function() {};
envision.Visualization.render = function() {};
envision.Visualization.remove = function() {};
envision.Visualization.setPosition = function() {};
envision.Visualization.indexOf = function() {};
envision.Visualization.getComponent = function() {};
envision.Visualization.isFirst = function() {};
envision.Visualization.isLast = function() {};
envision.Visualization.destroy = function() {};


// Preprocessor API

envision.Preprocessor = function () {};
envision.Preprocessor.getData = function () {};
envision.Preprocessor.setData = function () {};
envision.Preprocessor.length = function () {};
envision.Preprocessor.bound = function () {};
envision.Preprocessor.subsampleMinMax = function () {};
envision.Preprocessor.subsample = function () {};



// Interaction API

envision.Interaction = function () {};
envision.Interaction.leader = function () {};
envision.Interaction.follower = function () {};
envision.Interaction.group = function () {};
envision.Interaction.add = function () {};


