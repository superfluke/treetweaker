import mods.treetweaker.TreeFactory;

var oak = TreeFactory.createTree("oak");
oak.setLog("minecraft:wool:6");
oak.setLeaf("minecraft:stained_glass:2");
oak.minTreeHeight = 3;
oak.extraTreeHeight = 8;
oak.setTreeType("OAK");
oak.generationFrequency = 6;
oak.register();

var largeoak = TreeFactory.createTree("largeoak");
largeoak.setLog("minecraft:wool:3");
largeoak.setLeaf("minecraft:melon_block");
largeoak.extraTreeHeight = 6;
largeoak.minTreeHeight = 15;
largeoak.setTreeType("LARGE_OAK");
largeoak.generationFrequency = 8;
largeoak.extraThick = true;
largeoak.register();

var jungle = TreeFactory.createTree("jungle");
jungle.setLog("minecraft:bone_block");
jungle.setLeaf("minecraft:brown_mushroom_block");
jungle.setGenFrequency(7);
jungle.setTreeType("JUNGLE");
jungle.setMinHeight(10);
jungle.register();

var canopy = TreeFactory.createTree("canopy");
canopy.setLog("minecraft:stained_hardened_clay:12");
canopy.setLeaf("minecraft:hay_block");
canopy.setGenFrequency(4);
canopy.setTreeType("CANOPY");
canopy.setMinHeight(7);
canopy.setExtraHeight(8);
canopy.register();

var pine = TreeFactory.createTree("pine");
pine.setLeaf("minecraft:concrete:5");
pine.setGenFrequency(6);
pine.setTreeType("PINE");
pine.setMinHeight(18);
pine.setExtraHeight(0);
pine.register();

var acacia = TreeFactory.createTree("acacia");
acacia.setLog("minecraft:iron_block");
acacia.setLeaf("minecraft:gold_block");
//acacia.setGenFrequency(8);
acacia.setGenFrequency(4);
acacia.setTreeType("ACACIA");
acacia.setMinHeight(7);
acacia.setExtraHeight(7);
acacia.register();

var swampjungle = TreeFactory.createTree("swampjungle");
swampjungle.setGenBiome("minecraft:hell");
swampjungle.setTreeType("JUNGLE");
swampjungle.setLeaf("minecraft:leaves:3");
swampjungle.register();

var hillrocks = TreeFactory.createTree("rocky");
hillrocks.setGenBiome("minecraft:extreme_hills");
hillrocks.setTreeType("CANOPY");
hillrocks.setMinHeight(1);
hillrocks.setLeaf("minecraft:stone:2");
hillrocks.setLog("minecraft:stone:1");
hillrocks.register();

var errortree = TreeFactory.createTree("garbo");
errortree.setGenBiome("minecraft:notreal");
errortree.setLog("minecraft:errorrrrr");
errortree.setLeaf("minecraft:green_glazed_terracotta");
errortree.setMinHeight(0);
errortree.setExtraHeight(-2);
errortree.setGenFrequency(0);
errortree.setTreeType("NUMA_NUMA");
errortree.register();
