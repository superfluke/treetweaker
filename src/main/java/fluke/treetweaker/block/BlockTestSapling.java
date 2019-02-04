package fluke.treetweaker.block;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teamacronymcoders.base.client.models.generator.IHasGeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.GeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.IGeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.ModelType;

import fluke.treetweaker.TreeTweaker;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTestSapling extends BlockBush implements IGrowable, IGeneratedModel, IHasGeneratedModel
{
	public static final String REG_NAME = "testsapling";
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    protected WorldGenAbstractTree tree;
    
    public BlockTestSapling()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        setUnlocalizedName(TreeTweaker.MODID + ".testsapling");
        setRegistryName(REG_NAME);
        
        this.tree = new WorldGenSavannaTree(false);
    }
    
    public void setTreeInfo(WorldGenAbstractTree mrTree)
    {
    	this.tree = mrTree;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }
    
    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (((Integer)state.getValue(STAGE)).intValue() == 0)
        {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        }
        else
        {
            this.generateTree(worldIn, pos, state, rand);
        }
    }
    
    public void generateTree(World world, BlockPos pos, IBlockState state, Random rand)
    {
    	
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, pos)) 
        	return;
        
        //TODO dumbass sapling blocks the tree from growing
        this.tree.generate(world, rand, pos.up().east());
    }
    
//    @Override
//    protected boolean canSustainBush(IBlockState state)
//    {
//        return state.getBlock() == ModBlocks.endGrass || state.getBlock() == Blocks.END_STONE;
//    }
    
    /**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(STAGE).intValue() << 3;
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { STAGE });
	}
	
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state, rand);
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() 
	{
    	/*
		IStateMapper mappy = (new StateMap.Builder()).ignore(new IProperty[] { STAGE }).build();
		ModelLoader.setCustomStateMapper(this, mappy);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        */
	}

	@Override
	public String getName() {
		return REG_NAME;
	}

	@Override
	public ModelType getModelType() {
		return ModelType.BLOCKSTATE;
	}

	@Override
	public String getJson() {
		BufferedReader readIn = null;
		try {
			readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
				      .getResourceAsStream("assets/treetweaker/templates/block.txt"), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(readIn.toString());
		return readIn.toString();
	}
	
	@Override
    public List<IGeneratedModel> getGeneratedModels() {
        List<IGeneratedModel> models = Lists.newArrayList();

        //TemplateFile templateFile;
        //Map<String, String> replacements = Maps.newHashMap();

        //templateFile = TemplateManager.getTemplateFile("item_model_overlaid");
        
        //replacements.put("texture", "treetweaker:blocks/testsapling");
        //replacements.put("texture_overlay", "base:items/record_color");


        //templateFile.replaceContents(replacements);
        String templateJson = getJson();
        templateJson.replace("texture", "treetweaker:blocks/testsapling");
        models.add(new GeneratedModel(TreeTweaker.MODID + ":" + REG_NAME, getModelType(),
        		templateJson));

        return models;
    }

}
