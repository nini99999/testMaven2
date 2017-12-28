package com.extjs.service;

import com.extjs.model.EKnowledge;
import com.extjs.util.SysException;

import java.util.List;

public interface EKnowledgeService {
    EKnowledge getKnowledge(String id);
    /**
     * 根据条件查询记录
     *
     * @param knowledge
     * @return
     */
    List<EKnowledge> queryKnowledge(EKnowledge knowledge);



    /**
     * 查包含下级节点的树形结构
     *
     * @param id
     * @return
     */
    List<EKnowledge> getKnowledgeContainsChilds(String id);

    /**
     * 添加叶子节点之前，更新其父记录，设置其hasChild=1（包含子节点）
     *
     * @param knowledge
     * @throws SysException
     */
    EKnowledge addKnowledge(EKnowledge knowledge) throws SysException;

    void modifKnowledge(EKnowledge knowledge) throws SysException;

    void modifKnowledgeTextOnly(String id, String knowledgeText) throws SysException;

    /**
     * 删除时，需要同步删除指定id下的所有节点
     *
     * @param id
     * @throws SysException
     */
    void delKnowledge(String id) throws SysException;

    /**
     * 创建或查询树的根节点
     *
     * @param id
     */
    EKnowledge createRootNode(String parentID, String gradeno, String subjectno) throws SysException;


}
