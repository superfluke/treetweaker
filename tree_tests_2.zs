import mods.treetweaker.TreeFactory;

var forestacacia = TreeFactory.createTree("forestacacia");
forestacacia.setTreeType("ACACIA");
forestacacia.setLeaf("advancedrocketry:alienleaves");
forestacacia.setMinHeight(6);
forestacacia.setExtraHeight(5);
forestacacia.setGenFrequency(2);
forestacacia.setGenBiomeByTag("FOREST");
forestacacia.register();

var djungle = TreeFactory.createTree("darkjungle");
djungle.setTreeType("JUNGLE");
djungle.setLeaf("defiledlands:tenebra_leaves");
djungle.setLog("defiledlands:tenebra_log");
djungle.setGenFrequency(4);
djungle.setMinHeight(16);
djungle.setExtraHeight(9);
djungle.setGenBiome("defiledlands:plains_defiled");
djungle.setBaseBlock("defiledlands:grass_defiled");
djungle.register();

var mushpine = TreeFactory.createTree("shroomtree");
mushpine.setTreeType("PINE");
mushpine.setLeaf("minecraft:red_mushroom_block");
mushpine.setLog("minecraft:brown_mushroom_block");
mushpine.setMinHeight(7);
mushpine.register();

var blueoak = TreeFactory.createTree("blueoak");
blueoak.setTreeType("LARGE_OAK");
blueoak.setLog("advancedrocketry:alienwood");
blueoak.setLeaf("advancedrocketry:alienleaves");
blueoak.setExtraHeight(6);
blueoak.setGenFrequency(2);
blueoak.register();


/*
* MOON TREES
*/
var lightoak = TreeFactory.createTree("lightoak");
lightoak.setTreeType("OAK");
lightoak.setLog("natura:overworld_logs2:1");
lightoak.setLeaf("natura:nether_leaves");
lightoak.setGenFrequency(7);
lightoak.setGenBiome("advancedrocketry:moon");
lightoak.setBaseBlock("advancedrocketry:moonturf");
lightoak.register();

var pinkcanopy = TreeFactory.createTree("canopy");
pinkcanopy.setTreeType("CANOPY");
pinkcanopy.setLog("natura:overworld_logs:2");
pinkcanopy.setLeaf("natura:overworld_leaves2:3");
pinkcanopy.setGenFrequency(5);
pinkcanopy.setMinHeight(7);
pinkcanopy.setExtraHeight(8);
pinkcanopy.setGenBiome("advancedrocketry:moon");
pinkcanopy.setBaseBlock("advancedrocketry:moonturf");
pinkcanopy.register();

var moonacacia = TreeFactory.createTree("moonacacia");
moonacacia.setTreeType("ACACIA");
moonacacia.setLog("natura:overworld_logs2");
moonacacia.setLeaf("natura:nether_leaves2");
moonacacia.setMinHeight(4);
moonacacia.setExtraHeight(5);
moonacacia.setGenFrequency(7);
moonacacia.setGenBiome("advancedrocketry:moon");
moonacacia.setBaseBlock("advancedrocketry:moonturf");
moonacacia.register();

var moonoak = TreeFactory.createTree("moonoak");
moonoak.setTreeType("LARGE_OAK");
moonoak.setLog("defiledlands:tenebra_log");
moonoak.setLeaf("natura:nether_leaves:2");
moonoak.setExtraHeight(6);
moonoak.setMinHeight(12);
moonoak.setGenFrequency(8);
moonoak.extraThick = true;
moonoak.setGenBiome("advancedrocketry:moon");
moonoak.setBaseBlock("advancedrocketry:moonturf");
moonoak.register();

var moonpie = TreeFactory.createTree("moonpie");
moonpie.setTreeType("LARGE_PINE");
moonpie.setLeaf("natura:nether_leaves:1");
moonpie.setLog("natura:overworld_logs:3");
moonpie.setMinHeight(6);
moonpie.setExtraHeight(6);
moonpie.setGenFrequency(4);
moonpie.setGenBiome("advancedrocketry:moon");
moonpie.setBaseBlock("advancedrocketry:moonturf");
moonpie.register();

var moonshrub = TreeFactory.createTree("moonshrub");
moonshrub.setTreeType("CANOPY");
moonshrub.setLog("natura:overworld_logs2:3");
moonshrub.setLeaf("natura:overworld_leaves");
moonshrub.setMinHeight(2);
moonshrub.setExtraHeight(2);
moonshrub.setGenFrequency(3);
moonshrub.setGenBiome("advancedrocketry:moon");
moonshrub.setBaseBlock("advancedrocketry:moonturf");
moonshrub.register();


/*
* METAL FOREST TREES
*/
var bronzey = TreeFactory.createTree("bronzey");
bronzey.setTreeType("LARGE_SPRUCE");
bronzey.setLeaf("thermalfoundation:storage_alloy:3");
bronzey.setLog("cavern:cave_block:3");
bronzey.setMinHeight(14);
bronzey.setExtraHeight(6);
bronzey.setGenFrequency(7);
bronzey.setGenBiome("advancedrocketry:hotdryrock");
bronzey.setBaseBlock("advancedrocketry:hotturf");
bronzey.register();

var enderjungle = TreeFactory.createTree("enderjungle");
enderjungle.setTreeType("JUNGLE");
enderjungle.setLeaf("thermalfoundation:storage_alloy:7");
enderjungle.setLog("quark:biotite_block");
enderjungle.setGenFrequency(8);
enderjungle.setMinHeight(16);
enderjungle.setExtraHeight(9);
enderjungle.setGenBiome("advancedrocketry:hotdryrock");
enderjungle.setBaseBlock("advancedrocketry:hotturf");
enderjungle.register();

var platshrub = TreeFactory.createTree("platshrub");
platshrub.setTreeType("LARGE_OAK");
platshrub.setLog("minecraft:iron_block");
platshrub.setLeaf("thermalfoundation:storage:6");
platshrub.setExtraHeight(4);
platshrub.setMinHeight(4);
platshrub.setGenFrequency(7);
platshrub.setGenBiome("advancedrocketry:hotdryrock");
platshrub.setBaseBlock("advancedrocketry:hotturf");
platshrub.register();

var ironpine = TreeFactory.createTree("ironpine");
ironpine.setTreeType("LARGE_PINE");
ironpine.setLeaf("thermalfoundation:storage_alloy:2");
ironpine.setLog("minecraft:iron_block");
ironpine.setMinHeight(8);
ironpine.setExtraHeight(5);
ironpine.setGenBiome("advancedrocketry:hotdryrock");
ironpine.setBaseBlock("advancedrocketry:hotturf");
ironpine.register();