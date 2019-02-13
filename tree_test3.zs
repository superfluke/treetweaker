#loader preinit

import mods.treetweaker.TreeFactory;

/*
var rocktree = TreeFactory.createTree("rocktree");
rocktree.setLog("minecraft:purpur_block");
rocktree.setLeaf("minecraft:air");
rocktree.setTreeType("ACACIA");
rocktree.setMinHeight(7);
//rocktree.setGenBiomeByTag("MOUNTAIN");
//rocktree.setBaseBlock("minecraft:stone");
rocktree.register();
*/

var blueoak = TreeFactory.createTree("blueoak");
blueoak.setTreeType("LARGE_OAK");
blueoak.setLog("minecraft:brown_mushroom_block");
blueoak.setLeaf("minecraft:air");
blueoak.setExtraHeight(6);
blueoak.addSapling();
blueoak.register();


var helix = TreeFactory.createTree("bigfancytree");
helix.setTreeType("BRAIDED");
helix.setLog("minecraft:log:3");
helix.setLeaf("minecraft:leaves2:1");
helix.setMinHeight(8);
helix.setExtraHeight(3);
helix.setDimWhitelist(-1);
helix.setBaseBlock("minecraft:netherrack"); 
helix.setGenBiome("minecraft:hell");
helix.addSapling();
helix.register();
/*
var netheroak = TreeFactory.createTree("blueoak");
netheroak.setTreeType("OAK");
netheroak.setLog("minecraft:brown_mushroom_block");
netheroak.setLeaf("minecraft:bone_block");
netheroak.setDimWhitelist(-1);
netheroak.setBaseBlock("minecraft:netherrack"); 
netheroak.setGenBiome("minecraft:hell");
netheroak.register();
*/

var palmy = TreeFactory.createTree("palmy");
palmy.setTreeType("PALM");
palmy.setLog("minecraft:planks:3");
palmy.setLeaf("minecraft:leaves:3");
palmy.setExtraHeight(3);
palmy.setBaseBlock("minecraft:sand"); 
palmy.setGenBiome("minecraft:beaches");
palmy.addSapling();
palmy.register();

var tallcanopy = TreeFactory.createTree("bigcanopyboi");
tallcanopy.setTreeType("LARGE_CANOPY");
tallcanopy.setLog("minecraft:stained_hardened_clay:12");
tallcanopy.setLeaf("minecraft:stained_hardened_clay:5");
tallcanopy.setMinHeight(9);
tallcanopy.setExtraHeight(6);
tallcanopy.register();